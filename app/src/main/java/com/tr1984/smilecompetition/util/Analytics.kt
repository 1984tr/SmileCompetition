package com.tr1984.smilecompetition.util

import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object Analytics {

    private val instance = Firebase.analytics

    fun logEvent(name: String, params: Bundle = Bundle()) {
        instance.logEvent(name, params)
    }
}