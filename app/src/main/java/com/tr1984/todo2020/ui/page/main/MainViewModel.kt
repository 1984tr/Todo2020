package com.tr1984.todo2020.ui.page.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.tr1984.todo2020.data.TodoRepository
import com.tr1984.todo2020.data.entity.TodoEntity
import com.tr1984.todo2020.model.Todo
import com.tr1984.todo2020.ui.BaseViewModel
import kotlinx.coroutines.*
import java.util.*

class MainViewModel(repository: TodoRepository) : BaseViewModel(repository) {

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean>
        get() {
            return _refreshing
        }

    private val _items = MutableLiveData<List<Todo>>()
    val items: LiveData<List<Todo>>
        get() {
            return _items
        }
    val isEmptyMemo = Transformations.map(items) { it.isEmpty() }

    private val _selectedTodo = MutableLiveData<Todo>()
    val selectedTodo: LiveData<Todo>
        get() {
            return _selectedTodo
        }

    private val _expiredTodos = MutableLiveData(listOf<TodoEntity>())
    val expiredTodos: LiveData<List<TodoEntity>>
        get() {
            return _expiredTodos
        }

    private var isNotified = false

    fun selectTodo(todo: Todo) {
        _selectedTodo.postValue(todo)
    }

    fun fetch() {
        viewModelScope.launch {
            _refreshing.value = true

            val entities = repository.getAll()
            _items.value = entities.map { Todo(it) }

            val expired = entities.filter { it.expiredAt?.run {
                before(Date())
            } ?: false}
            if (!isNotified && expired.isNotEmpty()) {
                isNotified = true
                _expiredTodos.value = expired
            }
            _refreshing.value = false
        }
    }
}