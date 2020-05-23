package com.tr1984.todo2020.utils

import android.content.Context

class StringProvider(val context: Context) {

    fun getString(resId: Int) = context.getString(resId)
}