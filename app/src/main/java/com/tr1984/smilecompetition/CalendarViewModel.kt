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
                val cal = Calendar.getInstance().apply {
                    timeInMillis = histories[0].createdAt.time
                }
                val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
                if (dayOfWeek > 1) {
                    for (i in 1 .. dayOfWeek) {
                        ret.add(Smiling(false, Date()))
                    }
                }
                ret.addAll(histories)
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