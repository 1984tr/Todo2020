package com.tr1984.todo2020.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.tr1984.todo2020.R
import com.tr1984.todo2020.extensions.alert
import com.tr1984.todo2020.extensions.toast
import com.tr1984.todo2020.ui.dialog.LoadingDialog
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

open class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    protected val viewModel: T by lazy {
        getViewModel(getViewModelClass())
    }

    private val loadingDialog by lazy {
        LoadingDialog(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    protected open fun observeViewModel() {
        viewModel.run {
            notifier.observe(this@BaseActivity, Observer {
                when (it) {
                    is BaseViewModel.Notifier.Loading -> {
                        if (it.isStart) {
                            loadingDialog.show()
                        } else {
                            loadingDialog.dismiss()
                        }
                    }
                    is BaseViewModel.Notifier.Toast -> {
                        toast(it.message)
                    }
                    is BaseViewModel.Notifier.Alert -> {
                        alert(it.title, it.message, it.positive, it.negative)
                    }
                    is BaseViewModel.Notifier.Error -> {
                        alert(
                            resources.getString(R.string.notify),
                            it.throwable?.message ?: resources.getString(R.string.retry_later),
                            BaseViewModel.Notifier.Alert.Button(
                                resources.getString(R.string.submit),
                                it.onClick
                            )
                        )
                    }
                }
            })

            navigator.observe(this@BaseActivity, Observer {
                when (it) {
                    is BaseViewModel.Navigator.Start -> {
                        val intent = (it.intent ?: Intent()).apply {
                            setClass(this@BaseActivity, it.cls)
                        }
                        it.requestCode?.run {
                            startActivityForResult(intent, this)
                        } ?: startActivity(intent)
                    }
                    is BaseViewModel.Navigator.Finish -> {
                        it.resultCode?.let { rc ->
                            setResult(rc, it.intent)
                            finish()
                        } ?: finish()
                    }
                }
            })
        }
    }

    private fun getViewModelClass(): KClass<T> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return (type as Class<T>).kotlin
    }
}