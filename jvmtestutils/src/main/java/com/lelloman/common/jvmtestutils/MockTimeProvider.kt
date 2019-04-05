package com.lelloman.common.jvmtestutils

import com.lelloman.common.utils.TimeProvider
import com.lelloman.common.utils.model.*

class MockTimeProvider(var now: Long = 0) : TimeProvider {
    override fun nowUtcMs() = now

    override fun now() = DateTime(
        time = TimeImpl(0, 0, 0),
        date = DateImpl(0, 0, 0, DayOfTheWeek.SATURDAY)
    )

    override fun getTime(utcMs: Long): Time = now()

    override fun getDate(utcMs: Long): Date = now()

    override fun getDateTime(utcMs: Long): DateTime = now()
}