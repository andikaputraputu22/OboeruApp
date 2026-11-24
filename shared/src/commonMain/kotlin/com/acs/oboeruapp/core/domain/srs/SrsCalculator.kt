package com.acs.oboeruapp.core.domain.srs

enum class SrsRating {
    AGAIN,
    GOOD,
    EASY,
}

enum class ProgressStatus {
    NEW,
    LEARNING,
    MASTERED;

    companion object {
        fun fromString(value: String): ProgressStatus =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: NEW
    }
}

data class SrsState(
    val status: ProgressStatus = ProgressStatus.NEW,
    val srsStage: Int = 0,
    val nextReviewAt: Long? = null,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
)

object SrsCalculator {

    private const val MINUTE_MILLIS = 60_000L
    private const val DAY_MILLIS = 24L * 60L * MINUTE_MILLIS

    const val MAX_STAGE = 6
    const val MASTERED_STAGE_THRESHOLD = 3

    /**
     * Stage-to-interval mapping:
     * 0 -> 10 minutes (needs immediate reinforcement)
     * 1 -> 1 day
     * 2 -> 3 days
     * 3 -> 7 days (Mastered)
     * 4 -> 14 days
     * 5 -> 30 days
     * 6 -> 60 days
     */
    private val stageIntervalsMillis: List<Long> = listOf(
        10L * MINUTE_MILLIS,
        1L * DAY_MILLIS,
        3L * DAY_MILLIS,
        7L * DAY_MILLIS,
        14L * DAY_MILLIS,
        30L * DAY_MILLIS,
        60L * DAY_MILLIS,
    )

    fun intervalForStageMillis(stage: Int): Long =
        stageIntervalsMillis[stage.coerceIn(0, MAX_STAGE)]

    fun calculateNext(
        current: SrsState?,
        rating: SrsRating,
        nowEpochMillis: Long,
    ): SrsState {
        val base = current ?: SrsState()
        val currentStage = base.srsStage.coerceIn(0, MAX_STAGE)

        return when (rating) {
            SrsRating.AGAIN -> SrsState(
                status = ProgressStatus.LEARNING,
                srsStage = 0,
                nextReviewAt = nowEpochMillis + intervalForStageMillis(0),
                correctCount = base.correctCount,
                wrongCount = base.wrongCount + 1,
            )

            SrsRating.GOOD -> {
                val nextStage = (currentStage + 1).coerceAtMost(MAX_STAGE)
                SrsState(
                    status = if (nextStage >= MASTERED_STAGE_THRESHOLD) {
                        ProgressStatus.MASTERED
                    } else {
                        ProgressStatus.LEARNING
                    },
                    srsStage = nextStage,
                    nextReviewAt = nowEpochMillis + intervalForStageMillis(nextStage),
                    correctCount = base.correctCount + 1,
                    wrongCount = base.wrongCount,
                )
            }

            SrsRating.EASY -> {
                val nextStage = maxOf(currentStage + 2, MASTERED_STAGE_THRESHOLD)
                    .coerceAtMost(MAX_STAGE)
                SrsState(
                    status = ProgressStatus.MASTERED,
                    srsStage = nextStage,
                    nextReviewAt = nowEpochMillis + intervalForStageMillis(nextStage),
                    correctCount = base.correctCount + 1,
                    wrongCount = base.wrongCount,
                )
            }
        }
    }

    fun manualMasteryToggle(
        current: SrsState?,
        mastered: Boolean,
        nowEpochMillis: Long,
    ): SrsState {
        val base = current ?: SrsState()
        return if (mastered) {
            val nextStage = maxOf(base.srsStage, MASTERED_STAGE_THRESHOLD).coerceAtMost(MAX_STAGE)
            base.copy(
                status = ProgressStatus.MASTERED,
                srsStage = nextStage,
                nextReviewAt = nowEpochMillis + intervalForStageMillis(nextStage),
            )
        } else {
            base.copy(
                status = ProgressStatus.LEARNING,
                srsStage = 0,
                nextReviewAt = nowEpochMillis + intervalForStageMillis(0),
            )
        }
    }
}
