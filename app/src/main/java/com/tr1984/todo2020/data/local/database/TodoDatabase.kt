package com.tr1984.todo2020.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tr1984.todo2020.data.entity.TodoEntity

@Database(entities = [TodoEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {

        @Volatile
        private var instance: TodoDatabase? = null

        fun getInstance(context: Context): TodoDatabase {
            return instance ?: Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java, "todo-2020.db"
            ).build().also { instance = it }
        }
    }
}
