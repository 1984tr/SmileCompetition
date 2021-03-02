package com.tr1984.smilecompetition.util

import android.util.Log
import com.tr1984.smilecompetition.BuildConfig

object Logger {

    fun d(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d("1984tr", "😀 $msg")
        }
    }

    fun e(msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e("1984tr", "😡 $msg")
        }
    }
}