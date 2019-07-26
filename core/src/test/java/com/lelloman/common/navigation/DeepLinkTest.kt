package com.lelloman.common.navigation

import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test

class DeepLinkTest {

    private val tested = DeepLink(mock())

    @Test
    fun `puts and gets int value`() {
        val value = 1234
        val key = "asdasd"

        tested.putInt(key, value)

        assertThat(tested.getInt(key)).isEqualTo(value)
    }

    @Test
    fun `returns null Int if no value is set for key`() {
        assertThat(tested.getInt("meeeow")).isNull()
    }

    @Test
    fun `puts and gets boolean value`() {
        val key = "asdasd"

        tested.putBoolean(key, true)

        assertThat(tested.getBoolean(key)).isTrue()
    }

    @Test
    fun `returns null Boolean if no value is set for key`() {
        assertThat(tested.getBoolean("meeeow")).isNull()
    }

    @Test
    fun `puts and gets string value`() {
        val key = "blblbl"
        val value = "woof"

        tested.putString(key, value)

        assertThat(tested.getString(key)).isEqualTo(value)
    }

    @Test
    fun `returns null String if no value is set for key`() {
        assertThat(tested.getString("asdasd")).isNull()
    }

    @Test
    fun `puts and gets double value`() {
        val key = "blblbl"
        val value = 23232323.32323232

        tested.putDouble(key, value)

        assertThat(tested.getDouble(key)).isEqualTo(value)
    }

    @Test
    fun `returns null Double if no value is set for key`() {
        assertThat(tested.getDouble("asdasd")).isNull()
    }
}