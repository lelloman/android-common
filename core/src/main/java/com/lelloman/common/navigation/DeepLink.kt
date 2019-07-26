package com.lelloman.common.navigation

import android.net.Uri
import com.lelloman.common.utils.Base64Util
import java.io.*

@Suppress("unused")
class DeepLink(val screen: NavigationScreen) {

    private val parameters = mutableMapOf<String, Any>()

    val parametersCount get() = parameters.size

    private val stringRepresentation by lazy {
        val builder = Uri
            .Builder()
            .scheme(PROTOCOL)
            .appendPath(screen.name)

        getParametersList().forEach {
            builder.appendQueryParameter(it.first, it.second.toString())
        }
        builder.build().toString()
    }

    fun putString(key: String, value: String) = apply {
        parameters[key] = value
    }

    fun getString(key: String) = parameters[key] as String?

    fun putInt(key: String, value: Int) = apply {
        parameters[key] = value
    }

    fun getInt(key: String) = parameters[key] as Int?

    fun putBoolean(key: String, value: Boolean) = apply {
        parameters[key] = value
    }

    fun getBoolean(key: String) = parameters[key] as Boolean?

    fun putDouble(key: String, value: Double) = apply {
        parameters[key] = value
    }

    fun getDouble(key: String) = parameters[key] as Double?

    fun putSerializableArrayList(key: String, arrayList: ArrayList<out Serializable>) = apply {
        val bos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(bos)
        oos.writeObject(arrayList)
        val base64Encoded = Base64Util.encode(bos.toByteArray())
        parameters[key] = base64Encoded
    }

    fun <T : Serializable> getSerializableArrayList(key: String): ArrayList<T>? {
        return if (parameters.containsKey(key)) {
            val base64Encoded = parameters[key] as String
            val bytes = Base64Util.decode(base64Encoded)
            val ois = ObjectInputStream(ByteArrayInputStream(bytes))
            @Suppress("UNCHECKED_CAST")
            ois.readObject() as ArrayList<T>
        } else {
            null
        }
    }

    private fun getParametersList() = parameters
        .entries
        .asSequence()
        .map { it.key to it.value }
        .sortedBy { (key, _) -> key }
        .toList()

    override fun toString() = stringRepresentation

    companion object {
        const val PROTOCOL = "smurfs"
    }
}