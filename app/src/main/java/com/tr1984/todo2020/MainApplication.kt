package com.tr1984.todo2020

import android.app.Application
import com.tr1984.todo2020.data.repositoryModule
import com.tr1984.todo2020.ui.contextModules
import com.tr1984.todo2020.ui.viewModelModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(listOf(repositoryModule, viewModelModules, contextModules))
        }
    }
}