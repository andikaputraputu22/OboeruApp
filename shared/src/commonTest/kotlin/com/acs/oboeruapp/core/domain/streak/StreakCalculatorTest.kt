package com.acs.oboeruapp.core.domain.streak

import kotlin.test.Test
import kotlin.test.assertEquals

class StreakCalculatorTest {

    @Test
    fun returnsZeroWhenNoActiveDates() {
        assertEquals(0, StreakCalculator.calculateStreak(emptyList(), "2026-11-24"))
    }

    @Test
    fun countsConsecutiveDaysIncludingToday() {
        val dates = listOf("2026-11-24", "2026-11-23", "2026-11-22", "2026-11-20")
        assertEquals(3, StreakCalculator.calculateStreak(dates, "2026-11-24"))
    }

    @Test
    fun preservesActiveStreakFromYesterdayBeforeTodaySession() {
        val dates = listOf("2026-11-23", "2026-11-22", "2026-11-21")
        assertEquals(3, StreakCalculator.calculateStreak(dates, "2026-11-24"))
    }

    @Test
    fun returnsZeroWhenLastActivityWasTwoDaysAgo() {
        val dates = listOf("2026-11-22", "2026-11-21")
        assertEquals(0, StreakCalculator.calculateStreak(dates, "2026-11-24"))
    }
}
