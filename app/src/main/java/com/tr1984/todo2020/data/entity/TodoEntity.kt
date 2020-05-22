package com.tr1984.todo2020.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "todos")
data class TodoEntity(
    var title: String,
    var content: String?,
    var priority: Int,
    var createdAt: Date,
    var doneAt: Date? = null,
    var expiredAt: Date? = null
) {
    constructor() : this("", "", 0, Date(), null, null)

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}