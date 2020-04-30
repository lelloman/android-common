package com.lelloman.common.utils

import io.reactivex.functions.BiFunction

class PairBiFunction<T, U> : BiFunction<T, U, Pair<T, U>> {
    override fun apply(t1: T, t2: U): Pair<T, U> = t1 to t2
}