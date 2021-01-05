package com.tr1984.smilecompetition.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tr1984.smilecompetition.CalendarViewModel
import com.tr1984.smilecompetition.data.BePrettyDatabase

class ViewModelFactory(private val db: BePrettyDatabase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            CalendarViewModel(db) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}