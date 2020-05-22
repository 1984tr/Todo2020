package com.tr1984.todo2020.data.local

import com.tr1984.todo2020.data.TodoDataSource
import com.tr1984.todo2020.data.entity.TodoEntity
import java.util.*

class TodoTestDataSource : TodoDataSource {

    private var items = arrayListOf<TodoEntity>()

    override suspend fun getAll(): List<TodoEntity> {
        items = arrayListOf(
            TodoEntity(
                "title1",
                "content1",
                1,
                Date(System.currentTimeMillis()),
                null,
                null
            ).apply { id = 8 },
            TodoEntity(
                "title2",
                "content2",
                2,
                Date(System.currentTimeMillis()),
                null,
                Date(System.currentTimeMillis())
            ).apply { id = 1 },
            TodoEntity(
                "title3",
                "content3",
                3,
                Date(System.currentTimeMillis()),
                null,
                Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000)
            ).apply { id = 2 },
            TodoEntity(
                "title4",
                "content4",
                4,
                Date(System.currentTimeMillis()),
                null,
                Date(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000)
            ).apply { id = 3 },
            TodoEntity(
                "title5",
                "content5",
                5,
                Date(System.currentTimeMillis()),
                Date(System.currentTimeMillis()),
                null
            ).apply { id = 4 },
            TodoEntity(
                "title6",
                "content6",
                6,
                Date(System.currentTimeMillis()),
                Date(System.currentTimeMillis()),
                Date(System.currentTimeMillis())
            ).apply { id = 5 },
            TodoEntity(
                "title7",
                "content7",
                7,
                Date(System.currentTimeMillis()),
                Date(System.currentTimeMillis()),
                Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000)
            ).apply { id = 6 },
            TodoEntity(
                "title8",
                "content8",
                8,
                Date(System.currentTimeMillis()),
                Date(System.currentTimeMillis()),
                Date(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000)
            ).apply { id = 7 }
        )
        for (i in 9 until 100) {
            items.add(
                TodoEntity(
                    "title$i",
                    "content$i",
                    i,
                    Date(System.currentTimeMillis()),
                    null,
                    Date(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000)
                ).apply {
                    id =
                        i.toLong()
                })
        }
        return items
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