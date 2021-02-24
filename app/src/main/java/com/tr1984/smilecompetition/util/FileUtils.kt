package com.tr1984.smilecompetition.util

import android.content.Context
import android.os.Environment
import java.io.File

object FileUtils {

    fun getOutputDirectory(context: Context): File {
        val appContext = context.applicationContext
        val mediaDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let {
            it.apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else appContext.filesDir
    }

    fun createFile(baseFolder: File, name: String, extension: String) = File(baseFolder, name + extension)
}