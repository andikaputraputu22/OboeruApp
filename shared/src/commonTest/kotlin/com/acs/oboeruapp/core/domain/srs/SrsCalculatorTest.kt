package com.acs.oboeruapp.core.domain.srs

import kotlin.test.Test
import kotlin.test.assertEquals

class SrsCalculatorTest {

    private val now = 1_700_000_000_000L

    @Test
    fun ratingAgainResetsStageAndIncrementsWrongCount() {
        val current = SrsState(
            status = ProgressStatus.MASTERED,
            srsStage = 4,
            correctCount = 5,
            wrongCount = 1,
        )

        val result = SrsCalculator.calculateNext(
            current = current,
            rating = SrsRating.AGAIN,
            nowEpochMillis = now,
        )

        assertEquals(ProgressStatus.LEARNING, result.status)
        assertEquals(0, result.srsStage)
        assertEquals(5, result.correctCount)
        assertEquals(2, result.wrongCount)
        assertEquals(now + SrsCalculator.intervalForStageMillis(0), result.nextReviewAt)
    }

    @Test
    fun ratingGoodProgressesStageAndBecomesMasteredAtThreshold() {
        var state: SrsState? = null

        state = SrsCalculator.calculateNext(state, SrsRating.GOOD, now)
        assertEquals(1, state.srsStage)
        assertEquals(ProgressStatus.LEARNING, state.status)
        assertEquals(1, state.correctCount)

        state = SrsCalculator.calculateNext(state, SrsRating.GOOD, now)
        assertEquals(2, state.srsStage)
        assertEquals(ProgressStatus.LEARNING, state.status)

        state = SrsCalculator.calculateNext(state, SrsRating.GOOD, now)
        assertEquals(3, state.srsStage)
        assertEquals(ProgressStatus.MASTERED, state.status)
        assertEquals(3, state.correctCount)
        assertEquals(now + SrsCalculator.intervalForStageMillis(3), state.nextReviewAt)
    }

    @Test
    fun ratingGoodCapsAtMaxStage() {
        val current = SrsState(
            status = ProgressStatus.MASTERED,
            srsStage = SrsCalculator.MAX_STAGE,
            correctCount = 10,
            wrongCount = 0,
        )

        val result = SrsCalculator.calculateNext(current, SrsRating.GOOD, now)

        assertEquals(SrsCalculator.MAX_STAGE, result.srsStage)
        assertEquals(ProgressStatus.MASTERED, result.status)
        assertEquals(11, result.correctCount)
    }

    @Test
    fun ratingEasyJumpsDirectlyToMasteredFromNewItem() {
        val result = SrsCalculator.calculateNext(
            current = null,
            rating = SrsRating.EASY,
            nowEpochMillis = now,
        )

        assertEquals(SrsCalculator.MASTERED_STAGE_THRESHOLD, result.srsStage)
        assertEquals(ProgressStatus.MASTERED, result.status)
        assertEquals(1, result.correctCount)
        assertEquals(0, result.wrongCount)
        assertEquals(now + SrsCalculator.intervalForStageMillis(3), result.nextReviewAt)
    }

    @Test
    fun manualMasteryToggleSwitchesBetweenMasteredAndLearning() {
        val mastered = SrsCalculator.manualMasteryToggle(
            current = null,
            mastered = true,
            nowEpochMillis = now,
        )
        assertEquals(ProgressStatus.MASTERED, mastered.status)
        assertEquals(SrsCalculator.MASTERED_STAGE_THRESHOLD, mastered.srsStage)
        assertEquals(now + SrsCalculator.intervalForStageMillis(3), mastered.nextReviewAt)

        val unmastered = SrsCalculator.manualMasteryToggle(
            current = mastered,
            mastered = false,
            nowEpochMillis = now,
        )
        assertEquals(ProgressStatus.LEARNING, unmastered.status)
        assertEquals(0, unmastered.srsStage)
        assertEquals(now + SrsCalculator.intervalForStageMillis(0), unmastered.nextReviewAt)
    }
}
