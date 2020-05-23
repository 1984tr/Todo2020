package com.tr1984.todo2020.ui.page.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.tr1984.todo2020.data.TodoRepository
import com.tr1984.todo2020.data.entity.TodoEntity
import com.tr1984.todo2020.model.Todo
import com.tr1984.todo2020.ui.BaseViewModel
import com.tr1984.todo2020.utils.StringProvider
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(repository: TodoRepository, provider: StringProvider) : BaseViewModel(repository, provider) {

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

    fun selectTodo(todo: Todo) {
        _selectedTodo.postValue(todo)
    }

    fun fetch(withExpiredCheck: Boolean = false) {
        viewModelScope.launch {
            _refreshing.value = true

            val entities = repository.getAll()
            _items.value = entities.map { Todo(it, provider) }

            if (withExpiredCheck) {
                val expired = entities.filter { it.expiredAt?.run {
                    before(Date())
                } ?: false}
                if (expired.isNotEmpty()) {
                    _expiredTodos.value = expired
                }
            }
            _refreshing.value = false
        }
    }
}