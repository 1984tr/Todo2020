package com.tr1984.todo2020.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tr1984.todo2020.data.entity.TodoEntity
import java.text.SimpleDateFormat
import java.util.*

class Todo(val entity: TodoEntity) {

    private val todo = MutableLiveData(entity)
    val title = Transformations.map(todo) { it.title }
    val content = Transformations.map(todo) { it.content ?: "" }
    val dateText = Transformations.map(todo) {
        if (it.doneAt == null) {
            it.expiredAt?.run {
                when (val gapOfDate = getGapOfDate(Calendar.getInstance().time, this)) {
                    0 -> "D-Day"
                    else -> "D${if (gapOfDate > 0) "+" else ""}${gapOfDate}"
                }
            } ?: "작성일 ${SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(it.createdAt)}"
        } else {
            "완료일 ${SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(it.doneAt)}"
        }
    }
    val statusLevel = Transformations.map(todo) {
        if (it.doneAt == null) {
            it.expiredAt?.run {
                if (getGapOfDate(Calendar.getInstance().time, this) == 0) {
                    0
                } else {
                    if (time < System.currentTimeMillis()) {
                        2
                    } else {
                        0
                    }
                }
            } ?: 0
        } else {
            1
        }
    }

    private fun getGapOfDate(from: Date, to: Date): Int {
        val oneDay = 24 * 60 * 60 * 1000
        val fromMillis = from.time / oneDay
        val toMillis = to.time / oneDay
        return (fromMillis - toMillis).toInt()
    }
}