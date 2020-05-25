package com.tr1984.todo2020.data

import androidx.paging.DataSource
import com.tr1984.todo2020.data.entity.TodoEntity

interface TodoDataSource {

    fun getAll(): DataSource.Factory<Int, TodoEntity>

    suspend fun get(id: Long): TodoEntity?

    suspend fun insertOrUpdate(todo: TodoEntity)

    suspend fun delete(id: Long): Int

    suspend fun deleteAll(): Int
}