package com.acs.oboeruapp.features.kana.flashcard.ui

import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress
import com.acs.oboeruapp.features.kana.domain.model.KanaScript
import com.acs.oboeruapp.features.kana.flashcard.FlashcardFilter
import com.acs.oboeruapp.features.kana.flashcard.FlashcardSessionMode
import com.acs.oboeruapp.features.kana.flashcard.SrsIntervalHints

data class KanaFlashcardUiState(
    val isLoading: Boolean = true,
    val script: KanaScript = KanaScript.HIRAGANA,
    val selectedFilter: FlashcardFilter = FlashcardFilter.SMART,
    val currentCard: KanaItemWithProgress? = null,
    val cardTransitionKey: Int = 0,
    val isFlipped: Boolean = false,
    val isSubmittingRating: Boolean = false,
    val intervalHints: SrsIntervalHints? = null,
    val sessionMode: FlashcardSessionMode = FlashcardSessionMode.STANDALONE,
    val tracksFullDailyGoal: Boolean = false,
    val completedCards: Int = 0,
    val totalCards: Int = 0,
    val sessionReviewedCards: Int = 0,
    val againCount: Int = 0,
    val goodCount: Int = 0,
    val easyCount: Int = 0,
    val isSessionComplete: Boolean = false,
) {
    val progressFraction: Float
        get() = when {
            sessionMode == FlashcardSessionMode.FREE_PRACTICE -> 1f
            totalCards == 0 -> 0f
            else -> (completedCards.toFloat() / totalCards.toFloat()).coerceIn(0f, 1f)
        }

    val totalRatings: Int
        get() = againCount + goodCount + easyCount

    val accuracyPercent: Int
        get() = if (totalRatings == 0) 100 else (((goodCount + easyCount).toFloat() / totalRatings.toFloat()) * 100f).toInt()
}
