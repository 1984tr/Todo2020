package com.tr1984.todo2020.ui.page.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tr1984.todo2020.R
import com.tr1984.todo2020.data.TodoRepository
import com.tr1984.todo2020.data.entity.TodoEntity
import com.tr1984.todo2020.ui.BaseViewModel
import com.tr1984.todo2020.utils.StringProvider
import kotlinx.coroutines.launch
import java.util.*

class DetailViewModel(repository: TodoRepository, provider: StringProvider) :
    BaseViewModel(repository, provider) {

    val title = MutableLiveData("")
    val content = MutableLiveData("")
    var priority = MutableLiveData("")
    val date = MutableLiveData(provider.getString(R.string.empty))
    val isDone = MutableLiveData(false)

    private var todoEntity: TodoEntity? = null

    fun fetch(todoId: Long) {
        viewModelScope.launch {
            _notifier.value = Notifier.Loading(true)
            val entity = repository.get(todoId)
            entity?.let {
                todoEntity = it
                title.value = it.title
                content.value = it.content
                isDone.value = it.doneAt != null
                it.due?.let { date ->
                    val cal = Calendar.getInstance()
                    cal.time = date
                    this@DetailViewModel.date.value =
                        "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.MONTH) + 1}-${cal.get(Calendar.DATE)}"
                }
                priority.value = it.priority.toString()
            }
            _notifier.value = Notifier.Loading(false)
        }
    }

    fun submit(completion: () -> Unit) {
        viewModelScope.launch {
            _notifier.value = Notifier.Loading(true)
            val title = title.value ?: ""
            if (title.isEmpty()) {
                _notifier.value = Notifier.Toast(provider.getString(R.string.detail_enter_title))
                return@launch
            }
            val content = content.value ?: ""
            if (content.isEmpty()) {
                _notifier.value = Notifier.Toast(provider.getString(R.string.detail_enter_memo))
                return@launch
            }
            val entity = (todoEntity ?: TodoEntity()).apply {
                this.title = title
                this.content = content
                val priorityText = (this@DetailViewModel.priority.value ?: "")
                this.priority = (if (priorityText.isEmpty()) "0" else priorityText).toInt()
                if (isDone.value == true) {
                    this.doneAt = Date()
                } else {
                    this.doneAt = null
                }
            }
            repository.insertOrUpdate(entity)
            _notifier.value = Notifier.Loading(false)
            completion.invoke()
        }
    }

    fun delete(completion: () -> Unit) {
        viewModelScope.launch {
            _notifier.value = Notifier.Loading(true)
            todoEntity?.let {
                if (repository.delete(it.id) > 0) {
                    completion.invoke()
                } else {
                    _notifier.value = Notifier.Toast(provider.getString(R.string.retry_later))
                }
            }
            _notifier.value = Notifier.Loading(false)
        }
    }

    fun onDateSet(year: Int, month: Int, dayOfMonth: Int) {
        val cal = Calendar.getInstance()
        cal.set(year, month, dayOfMonth)
        date.value = "$year-${month + 1}-$dayOfMonth"

        val entity = todoEntity ?: TodoEntity().also {
            todoEntity = it
        }
        entity.due = cal.time
    }
}