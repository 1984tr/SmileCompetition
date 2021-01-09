package com.tr1984.smilecompetition.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "smiling", indices = [Index(value = ["createdAt"], unique = true)])
data class Smiling(
    @PrimaryKey(autoGenerate = true) val id: Long?, val checkIn: Boolean, val createdAt: Date?
) {
    constructor(checkIn: Boolean, createdAt: Date?) : this(null, checkIn, createdAt)

    fun date() : String {
        return SimpleDateFormat("MM/dd E").format(createdAt ?: Date())
    }
}