package com.tr1984.todo2020.extensions

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("goneIfTrue")
fun View.goneIfTrue(value: Boolean) {
    visibility = if (value) {
        View.GONE
    } else {
        View.VISIBLE
    }
}