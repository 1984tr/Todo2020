package com.tr1984.todo2020.ui

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tr1984.todo2020.data.TodoRepository
import com.tr1984.todo2020.ui.page.detail.DetailViewModel
import com.tr1984.todo2020.ui.page.main.MainViewModel
import com.tr1984.todo2020.utils.Logger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModules = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}

open class BaseViewModel(protected val repository: TodoRepository) : ViewModel() {

    val navigator: LiveData<Navigator>
        get() = _navigator
    val notifier: LiveData<Notifier>
        get() = _notifier

    protected val _navigator = MutableLiveData<Navigator>()
    protected val _notifier = MutableLiveData<Notifier>()

    init {
        Logger.d("BaseViewModel", "${this::class.simpleName} initialized ")
    }

    override fun onCleared() {
        super.onCleared()
        Logger.d("BaseViewModel", "${this::class.simpleName} cleared.")
    }

    sealed class Navigator {

        class Start(var cls: Class<*>, var requestCode: Int? = null, var intent: Intent? = null) :
            Navigator()

        class Finish(var resultCode: Int? = null, var intent: Intent? = null) : Navigator()
    }

    sealed class Notifier {

        class Loading(val isStart: Boolean) : Notifier()
        class Toast(val message: String) : Notifier()
        class Alert(
            val title: String?,
            val message: String,
            val positive: Button? = null,
            val negative: Button? = null
        ) : Notifier() {
            class Button(val label: String?, val onClick: (() -> Unit)? = null)
        }

        class Error(val throwable: Throwable?, val onClick: (() -> Unit)? = null) : Notifier()
    }
}