package com.tr1984.smilecompetition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ConfigViewModel  : ViewModel() {


    fun saveTime() {
        viewModelScope.launch {
            dataStore.edit {
                val key = preferencesKey<Long>("timer")
                it[key] = timer.toLong()
            }
        }
    }

    companion object {
        val timers = listOf(
            "5s" to 5 * 1000,
            "7s" to 7 * 1000,
            "10s" to 10 * 1000,
            "15s" to 15 * 1000,
            "20s" to 20 * 1000
        )
    }
}