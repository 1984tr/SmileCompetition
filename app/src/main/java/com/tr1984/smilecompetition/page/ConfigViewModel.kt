package com.tr1984.smilecompetition.page

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tr1984.smilecompetition.util.AlarmHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ConfigViewModel(private val dataStore: DataStore<Preferences>, private val alarmHelper: AlarmHelper) : ViewModel() {

    private val _duration = MutableLiveData(15)
    val duration: LiveData<Int>
        get() {
            return _duration
        }

    private val _hour = MutableLiveData(10)
    val hour : LiveData<Int>
        get() {
            return _hour
        }

    private val _minute = MutableLiveData(0)
    val minute : LiveData<Int>
        get() {
            return _minute
        }

    private val _ampm = MutableLiveData("am")
    val ampm : LiveData<String>
        get() {
            return _ampm
        }

    init {
        viewModelScope.launch {
            val preferences = dataStore.data.first()
            _duration.value = preferences[preferencesKey("duration")] ?: 15
            _hour.value = preferences[preferencesKey("hour")] ?: 15
            _minute.value = preferences[preferencesKey("minute")] ?: 15
            _ampm.value = preferences[preferencesKey("ampm")] ?: "am"
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
        _ampm.value = ampm
        saveDataStore("ampm", ampm.toLowerCase())
        regist()
    }

    private fun saveDataStore(key: String, value: Any) {
        viewModelScope.launch {
            dataStore.edit {
                it[preferencesKey(key)] = value
            }
        }
    }

    private fun regist() {
        val isAm = _ampm.value == "AM".toLowerCase()
        val hour = (_hour.value ?: 10) + (if (isAm) 0 else 12)
        val min = _minute.value ?: 10
        alarmHelper.regist(hour, min)
    }
}