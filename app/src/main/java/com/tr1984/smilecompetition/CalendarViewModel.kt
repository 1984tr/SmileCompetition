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

class CalendarViewModel(private val db: BePrettyDatabase, withInsert: Boolean) : ViewModel() {

    private val _histories = MutableLiveData(listOf<Smiling>())
    val histories: LiveData<List<Smiling>>
        get() {
            return _histories
        }

    init {
        viewModelScope.launch {
            if (withInsert) {
                insertToday()
            }
            val histories = db.dao().getAll()
            val ret = mutableListOf<Smiling>()
            if (histories.isNotEmpty()) {
                val cal = Calendar.getInstance()
                val start = histories[0].createdAt?.time ?: System.currentTimeMillis()
                val end = histories[histories.size-1].createdAt?.time ?: System.currentTimeMillis()
                val count = (end - start) / ( 24 * 60 * 60 * 1000) + 1
                Log.d("1984tr", "start: $start, end: $end, count: $count")

                for (i in 0 until count) {
                    val smiling = db.dao().get(Date(start))
                    if (smiling == null) {
                        ret.add(Smiling(false, null))
                    } else {
                        ret.add(smiling)
                    }
                }

                cal.timeInMillis = start
                val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
                Log.d("1984tr", "dayOfWeek: $dayOfWeek")
                if (dayOfWeek > Calendar.SUNDAY) {
                    for (i in Calendar.SUNDAY until dayOfWeek) {
                        ret.add(0, Smiling(false, null))
                    }
                }
                _histories.value = ret
            }
        }
    }

    private suspend fun insertToday() {
        val cal = Calendar.getInstance()
        val yyyy = cal.get(Calendar.YEAR)
        val mm = cal.get(Calendar.MONTH)
        val dd = cal.get(Calendar.DATE)
        cal.set(yyyy, mm, dd, 0, 0, 0)

        val smiling = Smiling(true, cal.time)
        db.dao().insert(smiling)
    }
}