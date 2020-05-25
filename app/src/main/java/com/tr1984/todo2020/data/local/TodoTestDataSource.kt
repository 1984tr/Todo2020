package com.tr1984.todo2020.data.local

import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.tr1984.todo2020.data.TodoDataSource
import com.tr1984.todo2020.data.entity.TodoEntity
import com.tr1984.todo2020.data.local.database.TodoDatabase
import java.util.*

class TodoTestDataSource(private val database: TodoDatabase) : TodoDataSource {

    private var items = arrayListOf<TodoEntity>()

    override fun getAll(): DataSource.Factory<Int, TodoEntity>  {
        return database.todoDao().getAll()
    }

    override suspend fun get(id: Long): TodoEntity? {
        return items.find { it.id == id }
    }

    override suspend fun insertOrUpdate(todo: TodoEntity) {
        items.find { it.id == todo.id }?.let {
            val index = items.indexOf(it)
            if (index >= 0) {
                items[index] = todo
            }
        } ?: items.add(todo)
    }

    override suspend fun delete(id: Long): Int {
        items.find { it.id == id }?.let {
            val index = items.indexOf(it)
            if (index >= 0) {
                items.removeAt(index)
                return 1
            }
        }
        return 0
    }

    override suspend fun deleteAll(): Int {
        items.clear()
        return 1
    }
}