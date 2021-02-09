package com.tr1984.smilecompetition.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.tr1984.smilecompetition.receiver.AlarmReceiver
import java.util.*

class AlarmHelper(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun regist(hourOfDay: Int, minute: Int, sec: Int) {
        cancelAlarm()

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, sec)
        }
        val timeInMillis =
            if (Date(calendar.timeInMillis).before(Date(System.currentTimeMillis()))) {
                calendar.timeInMillis + (24 * 60 * 60 * 1000)
            } else {
                calendar.timeInMillis
            }
        Log.d("1984tr", "$hourOfDay, $minute, $sec ,${Date(timeInMillis)}")
        val pi =
            PendingIntent.getBroadcast(context, 0, Intent(context, AlarmReceiver::class.java), 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pi
        )
    }

    private fun cancelAlarm() {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pi = PendingIntent.getService(
            context,
            NotifyHelper.NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
        if (pi != null) {
            alarmManager.cancel(pi)
        }
    }
}