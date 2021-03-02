package com.tr1984.smilecompetition.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.tr1984.smilecompetition.receiver.AlarmReceiver
import com.tr1984.smilecompetition.receiver.BootReceiver
import java.util.*


class AlarmHelper(private val context: Context) {

    fun add(hourOfDay: Int, minute: Int, sec: Int) {
        cancel()

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, sec)
        }

        while (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }
        
        Log.d(
            "1984tr",
            "$hourOfDay, $minute, $sec, ${calendar.timeInMillis}, ${Date(calendar.timeInMillis)}"
        )
        val pi = PendingIntent.getBroadcast(context, 0, Intent(context, AlarmReceiver::class.java), 0)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as? AlarmManager
        alarmManager?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setExact( AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
            } else {
                set( AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
            }
        }
    }

    private fun cancel() {
        val pi = PendingIntent.getBroadcast(context, 0, Intent(context, AlarmReceiver::class.java), 0)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as? AlarmManager
        alarmManager?.cancel(pi)
    }
}