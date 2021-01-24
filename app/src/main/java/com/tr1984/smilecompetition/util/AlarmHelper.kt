package com.tr1984.smilecompetition.util

import android.content.Context
import android.util.Log
import androidx.work.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.min

class AlarmHelper(private val context: Context) {

    fun regist(hourOfDay: Int, minute: Int ) {
        val workManager = WorkManager.getInstance(context)
        workManager.cancelAllWork()
        workManager.enqueue(createWorkRequest(hourOfDay, minute))
    }

    class DailyWorker(val context: Context, params: WorkerParameters) : Worker(context, params) {

        override fun doWork(): Result {
            val hourOfDay = inputData.getInt("hourOfDay", DEFAULT_HOUR)
            val minute = inputData.getInt("minute", DEFAULT_MINUTE)
            WorkManager.getInstance(applicationContext)
                .enqueue(createWorkRequest(hourOfDay, minute))
            val notifyHelper = NotifyHelper(context)
            notifyHelper.alarm()
            return Result.success()
        }
    }

    companion object {

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
                .setInputData(workDataOf("hourOfDay" to hourOfDay, "minute" to minute))
                .addTag("Be_pretty")
                .build()
        }
    }
}