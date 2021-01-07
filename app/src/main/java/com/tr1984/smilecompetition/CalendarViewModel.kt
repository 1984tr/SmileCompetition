package com.tr1984.smilecompetition

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tr1984.smilecompetition.data.BePrettyDatabase
import com.tr1984.smilecompetition.data.Smiling
import kotlinx.coroutines.launch
import java.util.*

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
            if (histories.isEmpty()) {
                val cal = Calendar.getInstance()
                val yyyy = cal.get(Calendar.YEAR)
                val mm = cal.get(Calendar.MONTH)
                val dd = cal.get(Calendar.DATE)
                cal.set(yyyy, mm, dd, 0, 0, 0)

                val smiling = Smiling(false, cal.time)
                db.dao().insert(smiling)
                _histories.value = listOf(smiling)
            } else {
                histories.forEach {
                    Log.d("1984tr", it.toString())
                }
                _histories.value = histories
            }
        }
    }
}