package com.tr1984.smilecompetition.page

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tr1984.smilecompetition.data.BePrettyDatabase
import com.tr1984.smilecompetition.data.Smiling
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

class CalendarViewModel(private val db: BePrettyDatabase, withInsert: Boolean) : ViewModel() {

    private val _histories = MutableLiveData(listOf<Smiling>())
    val histories: LiveData<List<Smiling>>
        get() {
            return _histories
        }

    private val _fileName = MutableLiveData("")
    val fileName: LiveData<String>
        get() {
            return _fileName
        }

    init {
        viewModelScope.launch {
            if (withInsert) {
                insertToday()
            }
            val histories = db.dao().getAll().reversed()
            val ret = mutableListOf<Smiling>()
            if (histories.isNotEmpty()) {
                val cal = Calendar.getInstance()
                val start = throwPoint(histories[0].createdAt?.time ?: System.currentTimeMillis())
                val end = throwPoint(histories[histories.size-1].createdAt?.time ?: System.currentTimeMillis())
                val count = ceil(((end - start) / ONE_DAY).toDouble()).toInt() + 1

                var day = start
                for (i in 0 until count) {
                    val smiling = db.dao().get(Date(day))
                    if (smiling == null) {
                        ret.add(Smiling(false, null))
                    } else {
                        ret.add(smiling)
                    }
                    day += ONE_DAY
                }

                cal.timeInMillis = start
                val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
                if (dayOfWeek > Calendar.SUNDAY) {
                    for (i in Calendar.SUNDAY until dayOfWeek) {
                        ret.add(0, Smiling(false, null))
                    }
                }
                _histories.value = ret
            }
        }
    }

    fun loadPicture(smiling: Smiling) {
        val cal = Calendar.getInstance().apply {
            timeInMillis = smiling.createdAt?.time ?: System.currentTimeMillis()
        }
        val yy = cal.get(Calendar.YEAR)
        val mm = cal.get(Calendar.MONTH) + 1
        val dd = cal.get(Calendar.DATE)
        val fileName = "BP_$yy$mm$dd.jpg"
        Log.d("1984tr", "fileName: $fileName")
        _fileName.value = fileName
    }

    private suspend fun insertToday() {
        val smiling = Smiling(true, getToday())
        db.dao().insert(smiling)
    }

    private fun getToday() : Date {
        return Date(throwPoint(System.currentTimeMillis()))
    }

    private fun throwPoint(timeInMillis: Long) : Long {
        return floor((timeInMillis / ONE_DAY).toDouble()).toLong() * ONE_DAY
    }

    companion object {
        val ONE_DAY = 24 * 60 * 60 * 1000
    }
}