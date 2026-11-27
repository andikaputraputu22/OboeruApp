package com.acs.oboeruapp.features.kana.flashcard

import com.acs.oboeruapp.core.domain.srs.ProgressStatus
import com.acs.oboeruapp.features.kana.domain.model.KanaCategory
import com.acs.oboeruapp.features.kana.domain.model.KanaItem
import com.acs.oboeruapp.features.kana.domain.model.KanaItemWithProgress
import com.acs.oboeruapp.features.kana.domain.model.KanaScript
import kotlin.test.Test
import kotlin.test.assertEquals

class KanaFlashcardSessionBuilderTest {

    @Test
    fun smartMixPrioritizesLearningThenNewThenMastered() {
        val items = listOf(
            sample("m1", KanaCategory.BASIC, sortOrder = 0, status = ProgressStatus.MASTERED, lastReviewedAt = 200L),
            sample("n2", KanaCategory.BASIC, sortOrder = 2, status = ProgressStatus.NEW),
            sample("n1", KanaCategory.BASIC, sortOrder = 1, status = ProgressStatus.NEW),
            sample("l2", KanaCategory.BASIC, sortOrder = 4, status = ProgressStatus.LEARNING, nextReviewAt = 500L),
            sample("l1", KanaCategory.BASIC, sortOrder = 3, status = ProgressStatus.LEARNING, nextReviewAt = 100L),
            sample("m0", KanaCategory.BASIC, sortOrder = 5, status = ProgressStatus.MASTERED, lastReviewedAt = 50L),
        )

        val queue = KanaFlashcardSessionBuilder.buildSessionQueue(
            allItems = items,
            filter = FlashcardFilter.SMART,
            sessionSize = 6,
        )

        assertEquals(
            listOf("l1", "l2", "n1", "n2", "m0", "m1"),
            queue.map { it.item.id },
        )
    }

    @Test
    fun categoryFilterScopesItemsAndRespectsSessionSize() {
        val items = listOf(
            sample("b1", KanaCategory.BASIC, sortOrder = 0),
            sample("d1", KanaCategory.DAKUTEN, sortOrder = 10),
            sample("d2", KanaCategory.DAKUTEN, sortOrder = 11),
            sample("d3", KanaCategory.DAKUTEN, sortOrder = 12),
            sample("y1", KanaCategory.YOON, sortOrder = 20),
        )

        val dakuten = KanaFlashcardSessionBuilder.buildSessionQueue(
            allItems = items,
            filter = FlashcardFilter.DAKUTEN,
            sessionSize = 2,
        )

        assertEquals(listOf("d1", "d2"), dakuten.map { it.item.id })
    }

    @Test
    fun requeueAfterAgainInsertsAtOffsetOrEndWhenQueueShorter() {
        val c1 = sample("c1", KanaCategory.BASIC, 1)
        val c2 = sample("c2", KanaCategory.BASIC, 2)
        val c3 = sample("c3", KanaCategory.BASIC, 3)
        val c4 = sample("c4", KanaCategory.BASIC, 4)
        val missed = sample("missed", KanaCategory.BASIC, 0)

        val withOffset = KanaFlashcardSessionBuilder.requeueAfterAgain(
            remainingQueue = listOf(c1, c2, c3, c4),
            missedCard = missed,
            offset = 3,
        )
        assertEquals(listOf("c1", "c2", "c3", "missed", "c4"), withOffset.map { it.item.id })

        val shortQueue = KanaFlashcardSessionBuilder.requeueAfterAgain(
            remainingQueue = listOf(c1),
            missedCard = missed,
            offset = 3,
        )
        assertEquals(listOf("c1", "missed"), shortQueue.map { it.item.id })

        val emptyQueue = KanaFlashcardSessionBuilder.requeueAfterAgain(
            remainingQueue = emptyList(),
            missedCard = missed,
            offset = 3,
        )
        assertEquals(listOf("missed"), emptyQueue.map { it.item.id })
    }

    @Test
    fun intervalHintsMatchSrsCalculatorStages() {
        val freshCard = sample("fresh", KanaCategory.BASIC, 0, status = ProgressStatus.NEW, srsStage = 0)
        val freshHints = KanaFlashcardSessionBuilder.intervalHintsFor(freshCard)
        assertEquals("10m", freshHints.againLabel)
        assertEquals("1d", freshHints.goodLabel)
        assertEquals("7d", freshHints.easyLabel)

        val masteredCard = sample("mastered", KanaCategory.BASIC, 1, status = ProgressStatus.MASTERED, srsStage = 3)
        val masteredHints = KanaFlashcardSessionBuilder.intervalHintsFor(masteredCard)
        assertEquals("10m", masteredHints.againLabel)
        assertEquals("14d", masteredHints.goodLabel)
        assertEquals("30d", masteredHints.easyLabel)
    }

    @Test
    fun resolveSessionPlanContinuesRemainingDailyGoalOrStartsFreePractice() {
        val continuing = KanaFlashcardSessionBuilder.resolveSessionPlan(
            dailyTarget = 15,
            completedToday = 5,
        )
        assertEquals(
            FlashcardSessionPlan(
                mode = FlashcardSessionMode.DAILY_GOAL,
                startingCompleted = 5,
                displayTotal = 15,
                cardsToStudy = 10,
            ),
            continuing,
        )

        val alreadyCompleted = KanaFlashcardSessionBuilder.resolveSessionPlan(
            dailyTarget = 15,
            completedToday = 16,
        )
        assertEquals(
            FlashcardSessionPlan(
                mode = FlashcardSessionMode.FREE_PRACTICE,
                startingCompleted = 16,
                displayTotal = 0,
                cardsToStudy = null,
            ),
            alreadyCompleted,
        )

        val forcedFree = KanaFlashcardSessionBuilder.resolveSessionPlan(
            dailyTarget = 15,
            completedToday = 5,
            forceFreePractice = true,
        )
        assertEquals(
            FlashcardSessionPlan(
                mode = FlashcardSessionMode.FREE_PRACTICE,
                startingCompleted = 5,
                displayTotal = 0,
                cardsToStudy = null,
            ),
            forcedFree,
        )

        val standalone = KanaFlashcardSessionBuilder.resolveSessionPlan(
            dailyTarget = null,
            completedToday = 0,
        )
        assertEquals(
            FlashcardSessionPlan(
                mode = FlashcardSessionMode.STANDALONE,
                startingCompleted = 0,
                displayTotal = 10,
                cardsToStudy = 10,
            ),
            standalone,
        )
    }

    @Test
    fun unlimitedSessionSizeReturnsAllScopedItems() {
        val items = listOf(
            sample("b1", KanaCategory.BASIC, 0),
            sample("b2", KanaCategory.BASIC, 1),
            sample("d1", KanaCategory.DAKUTEN, 2),
        )

        val allBasic = KanaFlashcardSessionBuilder.buildSessionQueue(
            allItems = items,
            filter = FlashcardFilter.BASIC,
            sessionSize = null,
        )

        assertEquals(listOf("b1", "b2"), allBasic.map { it.item.id })
    }

    private fun sample(
        id: String,
        category: KanaCategory,
        sortOrder: Int,
        status: ProgressStatus = ProgressStatus.NEW,
        srsStage: Int = 0,
        nextReviewAt: Long? = null,
        lastReviewedAt: Long? = null,
    ) = KanaItemWithProgress(
        item = KanaItem(
            id = id,
            script = KanaScript.HIRAGANA,
            category = category,
            groupKey = "a",
            character = "あ",
            romaji = "a",
            exampleWord = "あめ (ame)",
            exampleMeaning = "Rain",
            rowIndex = 0,
            colIndex = 0,
            sortOrder = sortOrder,
        ),
        status = status,
        srsStage = srsStage,
        nextReviewAt = nextReviewAt,
        lastReviewedAt = lastReviewedAt,
    )
}
