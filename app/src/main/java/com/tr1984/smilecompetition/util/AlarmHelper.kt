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
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }
        Log.d(
            "1984tr",
            "$hourOfDay, $minute, $sec, ${calendar.timeInMillis}, ${Date(calendar.timeInMillis)}"
        )
        val pi = PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, AlarmReceiver::class.java),
            0
        )
        val alarmManager = context.getSystemService(ALARM_SERVICE) as? AlarmManager
        alarmManager?.run {
            setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pi
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pi)
            }

            context.packageManager.setComponentEnabledSetting(
                ComponentName(
                    context,
                    BootReceiver::class.java
                ),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            );
        }
    }

    fun cancel() {
        val intent = Intent(context, AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as? AlarmManager
        alarmManager?.cancel(sender)
    }
}