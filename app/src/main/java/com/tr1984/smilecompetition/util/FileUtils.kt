package com.tr1984.smilecompetition.util

import android.content.Context
import android.os.Environment
import java.io.File
import java.util.*

object FileUtils {

    fun getOutputDirectory(context: Context): File {
        val appContext = context.applicationContext
        val mediaDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let {
            it.apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else appContext.filesDir
    }

    fun createFile(baseFolder: File) = File(baseFolder, getFileName(Calendar.getInstance()))

    fun getFileName(cal: Calendar) : String {
        val yy = cal.get(Calendar.YEAR)
        val mm = String.format("%02d", cal.get(Calendar.MONTH))
        val dd = String.format("%02d", cal.get(Calendar.DATE))
        return "BP_$yy$mm$dd.jpg"
    }
}