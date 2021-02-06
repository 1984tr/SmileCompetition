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
import kotlin.random.Random

class NotifyHelper(private val context: Context) {

    var notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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
        val message = messages[Random.nextInt(messages.size)]
        val builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_happy_noti)
                .setContentTitle(message.first)
                .setContentText(message.second)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pi)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
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
        const val NOTIFICATION_ID = 9876
        const val CHANNEL_ID = "be_pretty_channel_id"
        private val messages = listOf(
            "오늘도 예뻐질 시간이에요 😀" to "환하게 웃어봐요 🌼",
            "그거 아세요?" to "웃음이 면역력을 높여준데요! 💪",
            "환하게 웃어봐요 😃" to "스트레스가 사라질거에요",
            "행복해지려면 😍" to "활짝 웃어주세요 스마일~! 🌈",
            "매일매일 미소를 지으면" to "점점 더 예뻐진데요 ❤️",
            "사소하지만 하루 한번 미소가" to "인생을 바꿔줄지도 몰라요 😀",
            "가장 효과가 좋은 약은 💊 " to "바로 웃음이래요!"
        )
    }
}