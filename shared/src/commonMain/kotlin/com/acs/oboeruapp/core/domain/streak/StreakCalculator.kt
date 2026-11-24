package com.acs.oboeruapp.core.domain.streak

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

object StreakCalculator {

    /**
     * Calculates the current consecutive-day study streak.
     * If today already has activity, counts backwards from today.
     * Otherwise, if yesterday has activity, counts backwards from yesterday so an
     * active streak does not reset to 0 before the user completes today's session.
     */
    fun calculateStreak(
        activeIsoDates: List<String>,
        todayIsoDate: String,
    ): Int {
        if (activeIsoDates.isEmpty()) return 0

        val today = runCatching { LocalDate.parse(todayIsoDate) }.getOrNull() ?: return 0
        val activeDates = activeIsoDates
            .mapNotNull { runCatching { LocalDate.parse(it) }.getOrNull() }
            .toSet()

        val yesterday = today.minus(1, DateTimeUnit.DAY)
        var cursor = when {
            today in activeDates -> today
            yesterday in activeDates -> yesterday
            else -> return 0
        }

        var streak = 0
        while (cursor in activeDates) {
            streak++
            cursor = cursor.minus(1, DateTimeUnit.DAY)
        }
        return streak
    }
}
