package com.acs.oboeruapp.features.kana.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acs.oboeruapp.core.domain.srs.SrsCalculator
import com.acs.oboeruapp.core.domain.srs.SrsRating
import com.acs.oboeruapp.core.domain.srs.SrsState
import com.acs.oboeruapp.core.domain.time.TimeProvider
import com.acs.oboeruapp.features.deck.domain.repository.DeckRepository
import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress
import com.acs.oboeruapp.features.kana.domain.model.KanaScript
import com.acs.oboeruapp.features.kana.domain.model.StudyItemType
import com.acs.oboeruapp.features.kana.domain.repository.KanaRepository
import com.acs.oboeruapp.features.kana.domain.repository.ProgressRepository
import com.acs.oboeruapp.features.kana.flashcard.ui.KanaFlashcardUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class KanaFlashcardViewModel(
    private val script: KanaScript,
    private val blockId: String?,
    private val kanaRepository: KanaRepository,
    private val progressRepository: ProgressRepository,
    private val deckRepository: DeckRepository,
    private val timeProvider: TimeProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow(KanaFlashcardUiState(script = script))
    val uiState: StateFlow<KanaFlashcardUiState> = _uiState.asStateFlow()

    private var queue: ArrayDeque<KanaItemWithProgress> = ArrayDeque()
    private var transitionCounter: Int = 0
    private var sessionLoadGeneration: Int = 0
    private var loadJob: Job? = null
    private val isProcessingRating = MutableStateFlow(false)

    init {
        loadSession(filter = FlashcardFilter.SMART, forceFreePractice = false)
    }

    fun onFilterSelected(filter: FlashcardFilter) {
        if (isProcessingRating.value) return
        if (_uiState.value.selectedFilter == filter && !_uiState.value.isSessionComplete) return
        loadSession(filter = filter, forceFreePractice = false)
    }

    fun onFlipCard() {
        val current = _uiState.value
        if (current.isLoading || current.isSessionComplete || current.currentCard == null) return
        _uiState.update { it.copy(isFlipped = !it.isFlipped) }
    }

    fun onRateCard(rating: SrsRating) {
        val state = _uiState.value
        val activeCard = state.currentCard ?: return
        if (state.isLoading || state.isSessionComplete || !state.isFlipped) return
        if (!isProcessingRating.compareAndSet(expect = false, update = true)) return

        val capturedGeneration = sessionLoadGeneration
        _uiState.update { it.copy(isSubmittingRating = true) }

        viewModelScope.launch {
            try {
                progressRepository.recordReview(
                    itemType = StudyItemType.KANA,
                    itemId = activeCard.item.id,
                    rating = rating,
                )

                val passedCard = rating != SrsRating.AGAIN
                if (passedCard && blockId != null) {
                    deckRepository.incrementDailyLog(blockId = blockId, delta = 1)
                }

                // Ignore stale completion if user switched filter / restarted mid-write.
                if (capturedGeneration != sessionLoadGeneration) return@launch

                if (queue.isNotEmpty()) {
                    queue.removeFirst()
                }

                if (!passedCard) {
                    val updatedSrs = SrsCalculator.calculateNext(
                        current = SrsState(
                            status = activeCard.status,
                            srsStage = activeCard.srsStage,
                            nextReviewAt = activeCard.nextReviewAt,
                            correctCount = activeCard.correctCount,
                            wrongCount = activeCard.wrongCount,
                        ),
                        rating = SrsRating.AGAIN,
                        nowEpochMillis = timeProvider.nowEpochMillis(),
                    )
                    val updatedCard = activeCard.copy(
                        status = updatedSrs.status,
                        srsStage = updatedSrs.srsStage,
                        nextReviewAt = updatedSrs.nextReviewAt,
                        correctCount = updatedSrs.correctCount,
                        wrongCount = updatedSrs.wrongCount,
                    )
                    val requeued = KanaFlashcardSessionBuilder.requeueAfterAgain(
                        remainingQueue = queue.toList(),
                        missedCard = updatedCard,
                    )
                    queue = ArrayDeque(requeued)
                }

                val nextCard = queue.firstOrNull()
                transitionCounter++

                _uiState.update { current ->
                    current.copy(
                        currentCard = nextCard,
                        cardTransitionKey = transitionCounter,
                        isFlipped = false,
                        isSubmittingRating = false,
                        intervalHints = nextCard?.let { KanaFlashcardSessionBuilder.intervalHintsFor(it) },
                        completedCards = if (passedCard) current.completedCards + 1 else current.completedCards,
                        sessionReviewedCards = if (passedCard) current.sessionReviewedCards + 1 else current.sessionReviewedCards,
                        againCount = if (rating == SrsRating.AGAIN) current.againCount + 1 else current.againCount,
                        goodCount = if (rating == SrsRating.GOOD) current.goodCount + 1 else current.goodCount,
                        easyCount = if (rating == SrsRating.EASY) current.easyCount + 1 else current.easyCount,
                        isSessionComplete = nextCard == null,
                    )
                }
            } finally {
                isProcessingRating.value = false
                if (capturedGeneration == sessionLoadGeneration) {
                    _uiState.update { it.copy(isSubmittingRating = false) }
                }
            }
        }
    }

    fun onRestartSession() {
        if (isProcessingRating.value) return
        val forceFree = _uiState.value.tracksFullDailyGoal ||
            _uiState.value.sessionMode == FlashcardSessionMode.FREE_PRACTICE
        loadSession(filter = _uiState.value.selectedFilter, forceFreePractice = forceFree)
    }

    private fun loadSession(
        filter: FlashcardFilter,
        forceFreePractice: Boolean,
    ) {
        val generation = ++sessionLoadGeneration
        loadJob?.cancel()
        _uiState.update {
            it.copy(
                isLoading = true,
                selectedFilter = filter,
                isFlipped = false,
                isSubmittingRating = false,
                isSessionComplete = false,
            )
        }

        loadJob = viewModelScope.launch {
            val matchedBlock = if (blockId != null) {
                deckRepository.observeActiveBlocks().first().firstOrNull { it.id == blockId }
            } else {
                null
            }

            val plan = KanaFlashcardSessionBuilder.resolveSessionPlan(
                dailyTarget = matchedBlock?.dailyTarget,
                completedToday = matchedBlock?.completedToday ?: 0,
                forceFreePractice = forceFreePractice,
            )

            val allItems = kanaRepository.observeKanaWithProgress(script).first()
            if (generation != sessionLoadGeneration) return@launch

            val builtQueue = KanaFlashcardSessionBuilder.buildSessionQueue(
                allItems = allItems,
                filter = filter,
                sessionSize = plan.cardsToStudy,
            )
            queue = ArrayDeque(builtQueue)
            val firstCard = queue.firstOrNull()
            transitionCounter++

            val effectiveTotal = when (plan.mode) {
                FlashcardSessionMode.DAILY_GOAL -> plan.startingCompleted + builtQueue.size
                FlashcardSessionMode.FREE_PRACTICE -> 0
                FlashcardSessionMode.STANDALONE -> builtQueue.size
            }
            val tracksFullDailyGoal = plan.mode == FlashcardSessionMode.DAILY_GOAL &&
                effectiveTotal >= plan.displayTotal

            _uiState.value = KanaFlashcardUiState(
                isLoading = false,
                script = script,
                selectedFilter = filter,
                currentCard = firstCard,
                cardTransitionKey = transitionCounter,
                isFlipped = false,
                isSubmittingRating = false,
                intervalHints = firstCard?.let { KanaFlashcardSessionBuilder.intervalHintsFor(it) },
                sessionMode = plan.mode,
                tracksFullDailyGoal = tracksFullDailyGoal,
                completedCards = plan.startingCompleted,
                totalCards = effectiveTotal,
                sessionReviewedCards = 0,
                againCount = 0,
                goodCount = 0,
                easyCount = 0,
                isSessionComplete = firstCard == null,
            )
        }
    }
}
