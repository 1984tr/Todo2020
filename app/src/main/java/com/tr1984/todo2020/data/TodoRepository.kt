package com.tr1984.todo2020.data

import android.content.Context
import androidx.paging.DataSource
import com.tr1984.todo2020.data.entity.TodoEntity
import com.tr1984.todo2020.data.local.TodoLocalDataSource
import com.tr1984.todo2020.data.local.TodoTestDataSource
import com.tr1984.todo2020.data.local.database.TodoDatabase
import org.koin.dsl.module

val repositoryModule = module {
    single {
        TodoRepository(get())
    }
}

class TodoRepository(context: Context) :
    TodoDataSource {

    private val isTest: Boolean = false
    private val testDataSource by lazy { TodoTestDataSource(TodoDatabase.getInstance(context)) }
    private val local by lazy { TodoLocalDataSource(TodoDatabase.getInstance(context)) }

    override fun getAll(): DataSource.Factory<Int, TodoEntity> {
        return if (isTest) {
            testDataSource.getAll()
        } else {
            local.getAll()
        }
    }

    override suspend fun get(id: Long): TodoEntity? {
        return if (isTest) {
            testDataSource.get(id)
        } else {
            local.get(id)
        }
    }

    override suspend fun insertOrUpdate(todo: TodoEntity) {
        if (isTest) {
            testDataSource.insertOrUpdate(todo)
        } else {
            local.insertOrUpdate(todo)
        }
    }

    override suspend fun delete(id: Long): Int {
        return if (isTest) {
            testDataSource.delete(id)
        } else {
            local.delete(id)
        }
    }

    override suspend fun deleteAll(): Int {
        return if (isTest) {
            testDataSource.deleteAll()
        } else {
            local.deleteAll()
        }
    }
}