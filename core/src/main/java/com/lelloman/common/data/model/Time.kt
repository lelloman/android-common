package com.lelloman.common.data.model

import androidx.annotation.IntRange

interface Time {
    val hour: Int
    val minute: Int
    val second: Int
}

data class TimeImpl @Throws(IllegalArgumentException::class) constructor(
    @IntRange(from = 0L, to = 23) override val hour: Int,
    @IntRange(from = 0L, to = 59) override val minute: Int,
    @IntRange(from = 0L, to = 59) override val second: Int = 0
) : Time {
    init {
        if (hour < 0 || hour > 23) {
            throw IllegalArgumentException("Argument hour must be in range 0-23, got $hour instead.")
        }

        if (minute < 0 || minute > 59) {
            throw IllegalArgumentException("Argument minute must be in range 0-59, got $minute instead.")
        }

        if (second < 0 || second > 59) {
            throw IllegalArgumentException("Argument second must be in range 0-59, got $second instead.")
        }
    }
}