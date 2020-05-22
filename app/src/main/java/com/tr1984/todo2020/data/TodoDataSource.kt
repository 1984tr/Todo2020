package com.tr1984.todo2020.data

import com.tr1984.todo2020.data.entity.TodoEntity

interface TodoDataSource {

    suspend fun getAll(): List<TodoEntity>

    suspend fun get(id: Long): TodoEntity?

    suspend fun insertOrUpdate(todo: TodoEntity)

    suspend fun delete(id: Long): Int

    suspend fun deleteAll(): Int
}