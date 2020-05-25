package com.tr1984.todo2020.data.local

import androidx.paging.DataSource
import com.tr1984.todo2020.data.TodoDataSource
import com.tr1984.todo2020.data.entity.TodoEntity
import com.tr1984.todo2020.data.local.database.TodoDatabase

class TodoLocalDataSource(private val database: TodoDatabase) : TodoDataSource {

    override fun getAll(): DataSource.Factory<Int, TodoEntity> {
        return database.todoDao().getAll()
    }

    override suspend fun get(id: Long): TodoEntity? {
        return database.todoDao().get(id)
    }

    override suspend fun insertOrUpdate(todo: TodoEntity) {
        return database.todoDao().insertOrUpdate(todo)
    }

    override suspend fun delete(id: Long): Int {
        return database.todoDao().delete(id)
    }

    override suspend fun deleteAll(): Int {
        return database.todoDao().deleteAll()
    }
}