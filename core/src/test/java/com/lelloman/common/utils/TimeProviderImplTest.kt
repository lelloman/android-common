package com.lelloman.common.utils

import com.lelloman.common.utils.model.DayOfTheWeek
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.text.SimpleDateFormat

class TimeProviderImplTest {

    private val tested = TimeProviderImpl()

    @Test
    fun `returns sunday at 9 am`() {
        val timeMs = SDF.parse("2018/10/07 09:22:22").time

        val time = tested.getDateTime(timeMs)

        assertThat(time.dayOfTheWeek).isEqualTo(DayOfTheWeek.SUNDAY)
    }

    @Test
    fun `returns saturday at 9 pm`() {
        val timeMs = SDF.parse("2018/10/06 21:22:22").time

        val time = tested.getDateTime(timeMs)

        assertThat(time.dayOfTheWeek).isEqualTo(DayOfTheWeek.SATURDAY)
    }

    @Test
    fun `returns wednesday at midnight`() {
        val timeMs = SDF.parse("2000/01/05 00:00:00").time

        val time = tested.getDateTime(timeMs)

        assertThat(time.dayOfTheWeek).isEqualTo(DayOfTheWeek.WEDNESDAY)
    }

    @Test
    fun `returns tuesday at noon`() {
        val timeMs = SDF.parse("2004/02/03 12:59:59").time

        val time = tested.getDateTime(timeMs)

        assertThat(time.dayOfTheWeek).isEqualTo(DayOfTheWeek.TUESDAY)
    }

    private companion object {
        val SDF = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    }
}