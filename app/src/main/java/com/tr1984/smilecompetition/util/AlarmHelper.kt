package com.tr1984.smilecompetition.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.tr1984.smilecompetition.receiver.AlarmReceiver
import java.util.*

class AlarmHelper(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun regist(hourOfDay: Int, minute: Int, sec: Int) {
        val pi = PendingIntent.getBroadcast(
            context, NotifyHelper.NOTIFICATION_ID, Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pi)

        val repeatInterval: Long = AlarmManager.INTERVAL_DAY
        val timeInMillis = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, sec)
        }.timeInMillis

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, repeatInterval, pi)
    }
}