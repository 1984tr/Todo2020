package com.tr1984.todo2020.extensions

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.tr1984.todo2020.ui.BaseViewModel

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.alert(
    title: String?,
    message: String?,
    positive: BaseViewModel.Notifier.Alert.Button?,
    negative: BaseViewModel.Notifier.Alert.Button? = null
) {
    val builder = AlertDialog.Builder(this)
    title?.let { builder.setTitle(it) }
    message?.let { builder.setMessage(it) }
    positive?.let {
        builder.setPositiveButton(it.label ?: "") { _, _ ->
            it.onClick?.invoke()
        }
    }
    negative?.let {
        builder.setNegativeButton(it.label ?: "") { _, _ ->
            it.onClick?.invoke()
        }
    }
    builder.create().show()
}