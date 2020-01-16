package com.lelloman.common.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.reflect.KProperty

class LazyLiveData<T>(private val initFunction: (() -> Unit)? = null) {

    private var value: MutableLiveData<T>? = null

    operator fun getValue(myObject: Any, property: KProperty<*>): MutableLiveData<T> {
        if (value == null) {
            value = MutableLiveData()
            initFunction?.invoke()
        }

        return value!!
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T): Nothing =
        throw IllegalAccessException("Property already initialized")
}

open class FirstValueLiveData<T>(firstValue: T?) : MutableLiveData<T>() {
    init {
        if (firstValue != null) {
            value = firstValue
        }
    }
}

class StringLiveData(firstValue: String? = null) : FirstValueLiveData<String>(firstValue)

class IntLiveData(firstValue: Int? = null) : FirstValueLiveData<Int>(firstValue)

class LongLiveData(firstValue: Long? = null) : FirstValueLiveData<Long>(firstValue)

class BooleanLiveData(firstValue: Boolean? = null) : FirstValueLiveData<Boolean>(firstValue)

class ListLiveData<T>(firstValue: List<T>? = null) : FirstValueLiveData<List<T>>(firstValue)

class SetLiveData<T>(firstValue: Set<T>? = null) : FirstValueLiveData<Set<T>>(firstValue)

val <T>MutableLiveData<T>.immutable: LiveData<T>
    get() = this