package com.tr1984.smilecompetition.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tr1984.smilecompetition.util.NotifyHelper

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val helper = NotifyHelper(context)
        helper.alarm()
    }
}