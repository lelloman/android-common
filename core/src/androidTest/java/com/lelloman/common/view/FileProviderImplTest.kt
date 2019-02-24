package com.lelloman.common.view

import androidx.test.platform.app.InstrumentationRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

class FileProviderImplTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val tested = FileProviderImpl(context)

    @Before
    fun setUp() {
        context.cacheDir.listFiles()?.forEach { it.deleteRecursively() }
        context.filesDir.listFiles()?.forEach { it.deleteRecursively() }
    }

    @After
    fun tearDown() {
        context.cacheDir.listFiles()?.forEach { it.deleteRecursively() }
        context.filesDir.listFiles()?.forEach { it.deleteRecursively() }
    }

    @Test
    fun getFileFromCacheDir() {
        val fileName = "file.meow"
        val file = tested.getCacheFile(fileName)

        assertThat(file.exists()).isFalse()
        assertThat(file.createNewFile()).isTrue()
        assertThat(File(context.cacheDir, fileName).exists()).isTrue()
    }

    @Test
    fun getFileFromFilesDir() {
        val fileName = "file.meow"
        val file = tested.getInternalFile(fileName)

        assertThat(file.exists()).isFalse()
        assertThat(file.createNewFile()).isTrue()
        assertThat(File(context.filesDir, fileName).exists()).isTrue()
    }

    @Test
    fun deletesAllCacheFiles() {
        val dir1 = File(context.cacheDir, "dir1")
        assertThat(dir1.mkdir()).isTrue()
        val file1 = File(context.cacheDir, "file1")
        assertThat(file1.createNewFile()).isTrue()
        val file2 = File(dir1, "file2")
        assertThat(file2.createNewFile()).isTrue()

        assertThat(dir1.exists()).isTrue()
        assertThat(file1.exists()).isTrue()
        assertThat(file2.exists()).isTrue()
        tested.deleteAllCacheFiles().blockingAwait()

        assertThat(dir1.exists()).isFalse()
        assertThat(file1.exists()).isFalse()
        assertThat(file2.exists()).isFalse()
    }


    @Test
    fun deletesAllInternalFiles() {
        val dir1 = File(context.filesDir, "dir1")
        assertThat(dir1.mkdir()).isTrue()
        val file1 = File(context.filesDir, "file1")
        assertThat(file1.createNewFile()).isTrue()
        val file2 = File(dir1, "file2")
        assertThat(file2.createNewFile()).isTrue()

        assertThat(dir1.exists()).isTrue()
        assertThat(file1.exists()).isTrue()
        assertThat(file2.exists()).isTrue()
        tested.deleteAllInternalFiles().blockingAwait()

        assertThat(dir1.exists()).isFalse()
        assertThat(file1.exists()).isFalse()
        assertThat(file2.exists()).isFalse()
    }
}