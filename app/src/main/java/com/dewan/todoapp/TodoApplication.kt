package com.dewan.todoapp

import android.app.Application
import com.dewan.todoapp.util.log.DebugTree
import com.dewan.todoapp.util.log.ReleaseTree
import timber.log.Timber

class TodoApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(DebugTree()) else Timber.plant(ReleaseTree())
    }
}