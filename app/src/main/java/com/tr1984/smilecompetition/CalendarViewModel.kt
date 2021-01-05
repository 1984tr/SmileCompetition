package com.tr1984.smilecompetition

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tr1984.smilecompetition.data.BePrettyDatabase
import com.tr1984.smilecompetition.data.Smiling
import kotlinx.coroutines.launch

class CalendarViewModel(private val db: BePrettyDatabase) : ViewModel() {

    private val _histories = MutableLiveData(listOf<Smiling>())
    val histories: LiveData<List<Smiling>>
        get() {
            return _histories
        }

    init {
        viewModelScope.launch {
            val histories = db.dao().getAll()
            Log.d("1984tr", "histories: ${histories.size}")
        }
    }
}