package com.lelloman.common.utils

import androidx.databinding.ViewDataBinding

@Suppress("unused")
class StubViewDataBinding : ViewDataBinding(null, null, 0) {

    override fun setVariable(variableId: Int, value: Any?) = false

    override fun executeBindings() = Unit

    override fun onFieldChange(localFieldId: Int, `object`: Any?, fieldId: Int) = false

    override fun invalidateAll() = Unit

    override fun hasPendingBindings() = false
}