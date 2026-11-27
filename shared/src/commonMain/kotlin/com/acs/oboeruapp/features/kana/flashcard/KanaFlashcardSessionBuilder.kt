package com.acs.oboeruapp.features.kana.flashcard

import com.acs.oboeruapp.core.domain.srs.ProgressStatus
import com.acs.oboeruapp.core.domain.srs.SrsCalculator
import com.acs.oboeruapp.core.domain.srs.SrsRating
import com.acs.oboeruapp.core.domain.srs.SrsState
import com.acs.oboeruapp.features.kana.domain.model.KanaCategory
import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress

enum class FlashcardFilter(val label: String) {
    SMART("All Mix"),
    BASIC("Basic"),
    DAKUTEN("Dakuten"),
    YOON("Yoon"),
}

data class SrsIntervalHints(
    val againLabel: String,
    val goodLabel: String,
    val easyLabel: String,
)

enum class FlashcardSessionMode {
    DAILY_GOAL,
    FREE_PRACTICE,
    STANDALONE,
}

data class FlashcardSessionPlan(
    val mode: FlashcardSessionMode,
    val startingCompleted: Int,
    val displayTotal: Int,
    val cardsToStudy: Int?,
)

object KanaFlashcardSessionBuilder {

    const val DEFAULT_SESSION_SIZE = 10
    const val MIN_SESSION_SIZE = 5
    const val MAX_SESSION_SIZE = 30
    const val AGAIN_REQUEUE_OFFSET = 3

    fun resolveSessionPlan(
        dailyTarget: Int?,
        completedToday: Int,
        forceFreePractice: Boolean = false,
    ): FlashcardSessionPlan {
        if (dailyTarget == null || dailyTarget <= 0) {
            return FlashcardSessionPlan(
                mode = FlashcardSessionMode.STANDALONE,
                startingCompleted = 0,
                displayTotal = DEFAULT_SESSION_SIZE,
                cardsToStudy = DEFAULT_SESSION_SIZE,
            )
        }

        val clampedTarget = dailyTarget.coerceIn(MIN_SESSION_SIZE, MAX_SESSION_SIZE)
        val safeCompleted = completedToday.coerceAtLeast(0)

        return if (!forceFreePractice && safeCompleted < clampedTarget) {
            FlashcardSessionPlan(
                mode = FlashcardSessionMode.DAILY_GOAL,
                startingCompleted = safeCompleted,
                displayTotal = clampedTarget,
                cardsToStudy = clampedTarget - safeCompleted,
            )
        } else {
            FlashcardSessionPlan(
                mode = FlashcardSessionMode.FREE_PRACTICE,
                startingCompleted = safeCompleted,
                displayTotal = 0,
                cardsToStudy = null,
            )
        }
    }

    fun buildSessionQueue(
        allItems: List<KanaItemWithProgress>,
        filter: FlashcardFilter,
        sessionSize: Int? = DEFAULT_SESSION_SIZE,
    ): List<KanaItemWithProgress> {
        if (allItems.isEmpty()) return emptyList()

        val scoped = when (filter) {
            FlashcardFilter.SMART -> allItems
            FlashcardFilter.BASIC -> allItems.filter { it.item.category == KanaCategory.BASIC }
            FlashcardFilter.DAKUTEN -> allItems.filter { it.item.category == KanaCategory.DAKUTEN }
            FlashcardFilter.YOON -> allItems.filter { it.item.category == KanaCategory.YOON }
        }

        if (scoped.isEmpty()) return emptyList()

        val learning = scoped
            .filter { it.status == ProgressStatus.LEARNING }
            .sortedWith(compareBy<KanaItemWithProgress>({ it.nextReviewAt ?: Long.MIN_VALUE }, { it.item.sortOrder }))

        val fresh = scoped
            .filter { it.status == ProgressStatus.NEW }
            .sortedBy { it.item.sortOrder }

        val mastered = scoped
            .filter { it.status == ProgressStatus.MASTERED }
            .sortedWith(compareBy<KanaItemWithProgress>({ it.lastReviewedAt ?: Long.MIN_VALUE }, { it.item.sortOrder }))

        val prioritized = learning + fresh + mastered
        if (sessionSize == null) return prioritized
        val limit = sessionSize.coerceIn(1, prioritized.size)
        return prioritized.take(limit)
    }

    /**
     * Re-inserts a missed card a few positions ahead in the remaining queue
     * so the learner must recall it again before completing the session.
     */
    fun requeueAfterAgain(
        remainingQueue: List<KanaItemWithProgress>,
        missedCard: KanaItemWithProgress,
        offset: Int = AGAIN_REQUEUE_OFFSET,
    ): List<KanaItemWithProgress> {
        val insertIndex = offset.coerceIn(0, remainingQueue.size)
        return buildList(remainingQueue.size + 1) {
            addAll(remainingQueue.subList(0, insertIndex))
            add(missedCard)
            addAll(remainingQueue.subList(insertIndex, remainingQueue.size))
        }
    }

    fun intervalHintsFor(item: KanaItemWithProgress): SrsIntervalHints {
        val currentState = SrsState(
            status = item.status,
            srsStage = item.srsStage,
            nextReviewAt = item.nextReviewAt,
            correctCount = item.correctCount,
            wrongCount = item.wrongCount,
        )
        val againNext = SrsCalculator.calculateNext(currentState, SrsRating.AGAIN, 0L)
        val goodNext = SrsCalculator.calculateNext(currentState, SrsRating.GOOD, 0L)
        val easyNext = SrsCalculator.calculateNext(currentState, SrsRating.EASY, 0L)

        return SrsIntervalHints(
            againLabel = formatIntervalLabel(againNext.nextReviewAt ?: 0L),
            goodLabel = formatIntervalLabel(goodNext.nextReviewAt ?: 0L),
            easyLabel = formatIntervalLabel(easyNext.nextReviewAt ?: 0L),
        )
    }

    fun formatIntervalLabel(intervalMillis: Long): String {
        val minuteMillis = 60_000L
        val hourMillis = 60L * minuteMillis
        val dayMillis = 24L * hourMillis
        return when {
            intervalMillis < hourMillis -> "${(intervalMillis / minuteMillis).coerceAtLeast(1)}m"
            intervalMillis < dayMillis -> "${intervalMillis / hourMillis}h"
            else -> "${intervalMillis / dayMillis}d"
        }
    }
}
