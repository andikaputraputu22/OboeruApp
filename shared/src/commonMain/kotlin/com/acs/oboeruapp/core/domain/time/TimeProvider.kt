package com.acs.oboeruapp.core.domain.time

import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface TimeProvider {
    fun nowEpochMillis(): Long
    fun todayIsoDate(): String
}

class SystemTimeProvider : TimeProvider {

    override fun nowEpochMillis(): Long =
        Clock.System.now().toEpochMilliseconds()

    override fun todayIsoDate(): String =
        Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .toString()
}
