package com.tr1984.todo2020.data.local.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tr1984.todo2020.data.entity.TodoEntity

@Dao
interface TodoDao {

    @Query("SELECT * FROM todos ORDER BY priority DESC")
    fun getAll(): DataSource.Factory<Int, TodoEntity>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun get(id: Long): TodoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(todo: TodoEntity)

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun delete(id: Long) : Int

    @Query("DELETE FROM todos")
    suspend fun deleteAll() : Int
}