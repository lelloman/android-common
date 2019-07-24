@file:Suppress("unused")

package com.lelloman.common.utils

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import com.lelloman.common.navigation.DeepLink

val Context.layoutInflater: LayoutInflater get() = LayoutInflater.from(this)

fun DeepLink.putParcelable(key: String, parcelable: Parcelable) = apply {
    val parcel = Parcel.obtain()
    parcelable.writeToParcel(parcel, 0)
    val bytes = parcel.marshall()
    parcel.recycle()
    putString(key, Base64Util.encode(bytes))
}

fun <T : Parcelable> DeepLink.getParcelable(key: String, clazz: Class<T>): T? {
    val base64String = getString(key) ?: return null
    val bytes = Base64Util.decode(base64String)
    val parcel = Parcel.obtain()
    parcel.unmarshall(bytes, 0, bytes.size)
    parcel.setDataPosition(0)
    val creator = clazz.getField("CREATOR").get(null) as Parcelable.Creator<T>
    val output = creator.createFromParcel(parcel)
    parcel.recycle()
    return output
}
