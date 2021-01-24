package com.tr1984.smilecompetition.page

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tr1984.smilecompetition.util.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ConfigViewModel(private val dataStore: DataStore<Preferences>, private val alarmHelper: AlarmHelper) : ViewModel() {

    private val _duration = MutableLiveData(DEFAULT_DURATION)
    val duration: LiveData<Int>
        get() {
            return _duration
        }

    private val _hour = MutableLiveData(DEFAULT_HOUR)
    val hour : LiveData<Int>
        get() {
            return _hour
        }

    private val _minute = MutableLiveData(DEFAULT_MINUTE)
    val minute : LiveData<Int>
        get() {
            return _minute
        }

    private val _ampm = MutableLiveData(true)
    val ampm : LiveData<Boolean>
        get() {
            return _ampm
        }

    init {
        viewModelScope.launch {
            val preferences = dataStore.data.first()
            _duration.value = preferences[preferencesKey("duration")] ?: DEFAULT_DURATION
            _hour.value = preferences[preferencesKey("hour")] ?: DEFAULT_HOUR
            _minute.value = preferences[preferencesKey("minute")] ?: DEFAULT_MINUTE
            _ampm.value = (preferences[preferencesKey<String>("ampm")] ?: DEFAULT_AMPM) == DEFAULT_AMPM
        }
    }

    fun changeDuration(duration: Int) {
        _duration.value = duration
        saveDataStore("duration", duration)
    }

    fun changeHour(hour: Int) {
        _hour.value = hour
        saveDataStore("hour", hour)
        regist()
    }

    fun changeMinute(minute: Int) {
        _minute.value = minute
        saveDataStore("minute", minute)
        regist()
    }

    fun changeAmpm(ampm: String) {
        _ampm.value = ampm.toLowerCase() == DEFAULT_AMPM
        saveDataStore("ampm", ampm.toLowerCase())
        regist()
    }

    private fun saveDataStore(key: String, value: Int) {
        viewModelScope.launch {
            dataStore.edit {
                it[preferencesKey(key)] = value
            }
        }
    }

    private fun saveDataStore(key: String, value: String) {
        viewModelScope.launch {
            dataStore.edit {
                it[preferencesKey(key)] = value
            }
        }
    }

    private fun regist() {
        val isAm = _ampm.value ?: true
        val hour = (_hour.value ?: DEFAULT_HOUR) + (if (isAm) 0 else 12)
        val min = _minute.value ?: DEFAULT_MINUTE
        alarmHelper.regist(hour, min)
    }
}