package com.tr1984.smilecompetition.data

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun fromDate(date: Date?) = date?.time

    @TypeConverter
    fun toDate(millis: Long?) = millis?.let { Date(it) }
}