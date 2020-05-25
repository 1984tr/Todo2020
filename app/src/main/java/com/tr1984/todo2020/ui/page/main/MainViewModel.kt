package com.tr1984.todo2020.ui.page.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.toLiveData
import com.tr1984.todo2020.data.TodoRepository
import com.tr1984.todo2020.model.Todo
import com.tr1984.todo2020.ui.BaseViewModel
import com.tr1984.todo2020.utils.StringProvider
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(repository: TodoRepository, provider: StringProvider) :
    BaseViewModel(repository, provider) {

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean>
        get() {
            return _refreshing
        }

    val items = repository.getAll().map { Todo(it, provider) }
        .toLiveData(Config(pageSize = 30, enablePlaceholders = true, maxSize = 1000))
    val isEmptyMemo = Transformations.map(items) { it.isEmpty() }

    private val _selectedTodo = MutableLiveData<Todo>()
    val selectedTodo: LiveData<Todo>
        get() {
            return _selectedTodo
        }

    private val _expiredTodos = MutableLiveData(listOf<Todo>())
    val expiredTodos: LiveData<List<Todo>>
        get() {
            return _expiredTodos
        }

    fun selectTodo(todo: Todo) {
        _selectedTodo.postValue(todo)
    }

    fun refresh() {
        viewModelScope.launch {
            _refreshing.value = true

            items.value?.dataSource?.invalidate()

            _refreshing.value = false
        }
    }

    fun checkExpired() {
        val expired = items.value?.filter {
            it.entity.due?.run {
                before(Date())
            } ?: false
        }
        if (expired?.isNotEmpty() == true) {
            _expiredTodos.value = expired
        }
    }
}