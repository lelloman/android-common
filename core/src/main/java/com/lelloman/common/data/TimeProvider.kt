package com.lelloman.common.data

import com.lelloman.common.data.model.*
import com.lelloman.common.data.model.Date
import java.util.*

interface TimeProvider {

    fun nowUtcMs(): Long

    fun now(): DateTime

    fun getDateTime(utcMs: Long): DateTime

    fun getDate(utcMs: Long): Date

    fun getTime(utcMs: Long): Time
}

class TimeProviderImpl : TimeProvider {

    private val calendar: Calendar = Calendar.getInstance()

    private val time: Time
        get() = TimeImpl(
            hour = calendar.get(Calendar.HOUR_OF_DAY),
            minute = calendar.get(Calendar.MINUTE),
            second = calendar.get(Calendar.SECOND)
        )

    private val date: Date
        get() = DateImpl(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH),
            day = calendar.get(Calendar.DATE),
            dayOfTheWeek = DayOfTheWeek.fromCalendar(calendar)
        )

    override fun getDateTime(utcMs: Long): DateTime = withCalendarMs(utcMs) {
        DateTime(
            time = time,
            date = date
        )
    }

    override fun getDate(utcMs: Long): Date = withCalendarMs(utcMs, ::date)

    override fun getTime(utcMs: Long): Time = withCalendarMs(utcMs, ::time)

    override fun nowUtcMs() = System.currentTimeMillis()

    override fun now() = getDateTime(nowUtcMs())

    private fun <T> withCalendarMs(utcMs: Long, block: () -> T): T {
        calendar.timeInMillis = utcMs
        return block()
    }
}