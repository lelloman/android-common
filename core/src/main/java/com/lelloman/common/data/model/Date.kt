package com.lelloman.common.data.model

interface Date {
    val year: Int
    val month: Int
    val day: Int
    val dayOfTheWeek: DayOfTheWeek
}

data class DateImpl(
    override val year: Int,
    override val month: Int,
    override val day: Int,
    override val dayOfTheWeek: DayOfTheWeek
) : Date