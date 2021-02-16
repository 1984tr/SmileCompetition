package com.tr1984.smilecompetition.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.tr1984.smilecompetition.util.AlarmHelper
import com.tr1984.smilecompetition.util.NotifyHelper
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("1984tr", "AlarmReceiver.onReceive()")
        val helper = NotifyHelper(context)
        helper.alarm()
    }
}