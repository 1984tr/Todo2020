package com.tr1984.todo2020.utils

import android.util.Log
import com.tr1984.todo2020.BuildConfig

object Logger {

    fun d(tag: String, msg: String) {
        if (!BuildConfig.DEBUG) {
            return
        }
        Log.d(tag, msg)
    }
}