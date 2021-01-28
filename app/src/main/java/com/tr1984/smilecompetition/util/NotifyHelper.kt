package com.tr1984.smilecompetition.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.tr1984.smilecompetition.R
import com.tr1984.smilecompetition.page.MainActivity

class NotifyHelper(private val context: Context) {

    var notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    fun alarm() {
        val pi = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_happy_noti)
                .setContentTitle("당신은 웃을 때 가장 예뻐요.")
                .setContentText("오늘도 예뻐질 시간이에요 😀")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pi)
        notificationManager.notify(9876, builder.build())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(CHANNEL_ID, "일반", NotificationManager.IMPORTANCE_HIGH)
                    .apply {
                        lightColor = Color.RED
                        enableLights(true)
                        enableVibration(true)
                        description = "알람입니다."
                    }
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "be_pretty_channel_id"
    }
}