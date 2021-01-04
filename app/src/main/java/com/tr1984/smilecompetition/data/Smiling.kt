package com.tr1984.smilecompetition.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "smiling")
data class Smiling(
    @PrimaryKey(autoGenerate = true) val id: Long, val checkIn: Boolean, val createdAt: Date
) {
    fun date() : String {
        return SimpleDateFormat("MM/dd E").format(createdAt)
    }
}