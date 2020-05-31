package com.dewan.todoapp.util.log

import android.util.Log
import timber.log.Timber

class ReleaseTree: Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return !(priority == Log.INFO || priority == Log.VERBOSE || priority == Log.DEBUG)
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        /*if (priority == Log.ERROR) {
            //do some log
        }else if (priority == Log.WARN) {
            //do some thing
        }*/
    }
}