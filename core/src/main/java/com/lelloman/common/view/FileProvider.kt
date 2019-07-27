package com.lelloman.common.view

import android.content.Context
import io.reactivex.Completable
import java.io.File
import java.io.InputStream

interface FileProvider {

    fun openAssetsFile(fileName: String): InputStream

    fun getCacheFile(relativePath: String): File

    fun getInternalFile(relativePath: String): File

    fun getAbsoluteFile(absolutePath: String): File

    fun deleteAllCacheFiles(): Completable

    fun deleteAllInternalFiles(): Completable

    fun copyFile(src: File, dst: File): Completable
}

internal class FileProviderImpl(
    private val context: Context
) : FileProvider {

    override fun openAssetsFile(fileName: String): InputStream = context
        .assets
        .open(fileName)

    override fun getCacheFile(relativePath: String) = File(context.cacheDir, relativePath)

    override fun getInternalFile(relativePath: String) = File(context.filesDir, relativePath)

    override fun getAbsoluteFile(absolutePath: String) = File(absolutePath)

    override fun deleteAllCacheFiles() = Completable.fromAction {
        context.cacheDir.listFiles()?.forEach { it.deleteRecursively() }
    }

    override fun deleteAllInternalFiles() = Completable.fromAction {
        context.filesDir.listFiles()?.forEach { it.deleteRecursively() }
    }

    override fun copyFile(src: File, dst: File) = Completable.fromAction {
        src.copyTo(dst)
    }
}