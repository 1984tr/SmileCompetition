package com.tr1984.smilecompetition.util

import android.content.Context
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmHelper(val context: Context) {

    fun regist(hourOfDay: Int, minute: Int ) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWork()
        workManager.enqueue(createWorkRequest(hourOfDay, minute))
    }

    private fun createWorkRequest(hourOfDay: Int, minute: Int) : WorkRequest {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        dueDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
        dueDate.set(Calendar.MINUTE, minute)
        dueDate.set(Calendar.SECOND, 0)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }
        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        return OneTimeWorkRequestBuilder<DailyWorker>()
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .addTag("Be_pretty")
            .build()
    }

    class DailyWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {
        override fun doWork(): Result {
            val currentDate = Calendar.getInstance()
            val dueDate = Calendar.getInstance()
            // Set Execution around 05:00:00 AM
            dueDate.set(Calendar.HOUR_OF_DAY, 9)
            dueDate.set(Calendar.MINUTE, 30)
            dueDate.set(Calendar.SECOND, 0)
            if (dueDate.before(currentDate)) {
                dueDate.add(Calendar.HOUR_OF_DAY, 24)
            }
            val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
            val dailyWorkRequest = OneTimeWorkRequestBuilder<DailyWorker>()
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .addTag("Be_pretty")
                .build()
            WorkManager.getInstance(applicationContext)
                .enqueue(dailyWorkRequest)
            return Result.success()
        }
    }
}