package com.lelloman.common.data.model

import java.util.*

enum class DayOfTheWeek {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;

    companion object {

        fun fromCalendar(calendar: Calendar) = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> MONDAY
            Calendar.TUESDAY -> TUESDAY
            Calendar.WEDNESDAY -> WEDNESDAY
            Calendar.THURSDAY -> THURSDAY
            Calendar.FRIDAY -> FRIDAY
            Calendar.SATURDAY -> SATURDAY
            Calendar.SUNDAY -> SUNDAY
            else -> throw IllegalStateException("Invalid day of the week ${calendar.get(Calendar.DAY_OF_WEEK)}returned from Calendar.")
        }
    }
}