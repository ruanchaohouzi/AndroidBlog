package com.ruanchao.mvvmdemo

import android.app.Application
import android.content.Context

class MainApplication: Application() {

    companion object {
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

}