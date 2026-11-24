package com.acs.oboeruapp

import com.acs.oboeruapp.features.deck.domain.model.DeckActivityType
import com.acs.oboeruapp.features.deck.domain.model.DeckTopic
import com.acs.oboeruapp.features.deck.domain.model.UserDeckBlock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SharedCommonTest {

    @Test
    fun deckBlockProgressAndDisplayTitleWorkAsExpected() {
        val flashcardBlock = UserDeckBlock(
            id = "starter_hiragana_flashcard",
            topic = DeckTopic.HIRAGANA,
            activityType = DeckActivityType.FLASHCARD,
            dailyTarget = 10,
            sortOrder = 1,
            completedToday = 5,
        )
        assertEquals("Hiragana · Flashcards", flashcardBlock.displayTitle)
        assertTrue(flashcardBlock.isGoalBased)
        assertFalse(flashcardBlock.isCompletedToday)
        assertEquals(0.5f, flashcardBlock.progressFraction)

        val completedBlock = flashcardBlock.copy(completedToday = 12)
        assertTrue(completedBlock.isCompletedToday)
        assertEquals(1.0f, completedBlock.progressFraction)

        val chartBlock = UserDeckBlock(
            id = "starter_hiragana_chart",
            topic = DeckTopic.HIRAGANA,
            activityType = DeckActivityType.CHART,
            dailyTarget = 0,
            sortOrder = 0,
        )
        assertFalse(chartBlock.isGoalBased)
        assertNull(chartBlock.progressFraction)
    }
}
