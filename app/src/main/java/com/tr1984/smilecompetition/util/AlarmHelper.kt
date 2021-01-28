package com.tr1984.smilecompetition.util

import android.content.Context
import android.util.Log
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.min

class AlarmHelper(private val context: Context) {

    fun regist(hourOfDay: Int, minute: Int, sec: Int) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWork()
        workManager.enqueue(createWorkRequest(hourOfDay, minute, sec))
    }

    private fun createWorkRequest(hourOfDay: Int, minute: Int, sec: Int) : WorkRequest {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        dueDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
        dueDate.set(Calendar.MINUTE, minute)
        dueDate.set(Calendar.SECOND, sec)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        return OneTimeWorkRequestBuilder<DailyWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .build()
    }

    class DailyWorker(val context: Context, params: WorkerParameters) : Worker(context, params) {

        override fun doWork(): Result {
            val notifyHelper = NotifyHelper(context)
            notifyHelper.alarm()

            val workRequest = OneTimeWorkRequestBuilder<DailyWorker>()
                .setInitialDelay(24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
            return Result.success()
        }
    }
}