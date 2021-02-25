package com.tr1984.smilecompetition.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import com.tr1984.smilecompetition.util.AlarmHelper
import com.tr1984.smilecompetition.util.DEFAULT_AMPM
import com.tr1984.smilecompetition.util.DEFAULT_HOUR
import com.tr1984.smilecompetition.util.DEFAULT_MINUTE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            GlobalScope.launch(Dispatchers.IO){
                val dataStore = context?.createDataStore("smile_config")
                val preferences = dataStore?.data?.first()
                preferences?.run {
                    val hour = this[preferencesKey<Int>("hour")] ?: DEFAULT_HOUR
                    val minute = this[preferencesKey<Int>("minute")] ?: DEFAULT_MINUTE
                    val ampm = (this[preferencesKey<String>("ampm")] ?: DEFAULT_AMPM) == DEFAULT_AMPM

                    val alarmHelper = AlarmHelper(context)
                    alarmHelper.add(hour + (if (ampm) 0 else 12), minute, 0)
                }
            }
        }
    }
}