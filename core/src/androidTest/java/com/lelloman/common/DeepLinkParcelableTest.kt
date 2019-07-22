package com.lelloman.common

import android.os.Parcel
import android.os.Parcelable
import com.lelloman.common.navigation.DeepLink
import com.lelloman.common.navigation.DeepLinkStartable
import com.lelloman.common.navigation.NavigationScreen
import com.lelloman.common.utils.getParcelable
import com.lelloman.common.utils.putParcelable
import org.junit.Assert.assertEquals
import org.junit.Test

class DeepLinkParcelableTest {

    @Test
    fun convertsObject1() {
        val object1 = ParcelableObject(123, "asdasd", null)
        val deepLink = DeepLink(SCREEN).putParcelable(KEY_1, object1)

        val reconstructedObject1 = deepLink.getParcelable(KEY_1, ParcelableObject::class.java)

        assertEquals(object1, reconstructedObject1)
    }

    @Test
    fun convertsObject2() {
        val object1 = ParcelableObject(666, "meeeeow", null)
        val object2 = ParcelableObject(123, "asdasd", object1)
        val deepLink = DeepLink(SCREEN).putParcelable(KEY_1, object2)

        val reconstructedObject2 = deepLink.getParcelable(KEY_1, ParcelableObject::class.java)

        assertEquals(object2, reconstructedObject2)
    }

    @Test
    fun converts2Objects() {
        val object1 = ParcelableObject(Long.MAX_VALUE, "", null)
        val object2 = ParcelableObject(123, "asdasd", ParcelableObject(666, "meeeeow", null))
        val deepLink = DeepLink(SCREEN)
            .putParcelable(KEY_1, object1)
            .putParcelable(KEY_2, object2)

        val reconstructedObject1 = deepLink.getParcelable(KEY_1, ParcelableObject::class.java)
        val reconstructedObject2 = deepLink.getParcelable(KEY_2, ParcelableObject::class.java)

        assertEquals(object1, reconstructedObject1)
        assertEquals(object2, reconstructedObject2)
    }

    private companion object {
        val SCREEN = object : NavigationScreen {
            override val deepLinkStartable: DeepLinkStartable get() = TODO("not implemented")
            override val name: String get() = TODO("not implemented")
        }
        const val KEY_1 = "Key1"
        const val KEY_2 = "Key2"
    }
}

data class ParcelableObject(
    val value1: Long,
    val value2: String,
    val value3: ParcelableObject?
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString()!!,
        source.readParcelable<ParcelableObject>(ParcelableObject::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(value1)
        writeString(value2)
        writeParcelable(value3, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ParcelableObject> = object : Parcelable.Creator<ParcelableObject> {
            override fun createFromParcel(source: Parcel): ParcelableObject = ParcelableObject(source)
            override fun newArray(size: Int): Array<ParcelableObject?> = arrayOfNulls(size)
        }
    }
}