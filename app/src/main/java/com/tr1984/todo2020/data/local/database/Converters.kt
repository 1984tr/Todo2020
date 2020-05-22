package com.tr1984.todo2020.data.local.database

import androidx.room.TypeConverter
import com.tr1984.todo2020.data.entity.TodoEntity
import java.util.*

class Converters {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millis: Long?): Date? {
        return millis?.let { Date(it) }
    }
}