package com.tr1984.smilecompetition.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tr1984.smilecompetition.util.AlarmHelper
import com.tr1984.smilecompetition.util.NotifyHelper
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val helper = NotifyHelper(context)
        helper.alarm()

//        val alarmHelper = AlarmHelper(context)
//        val cal = Calendar.getInstance()
//        alarmHelper.regist(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE) + 1, 0)
    }
}