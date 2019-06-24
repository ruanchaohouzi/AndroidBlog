package com.ruanchao.androidblog

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

class MainApplication: Application() {

    companion object {
        var context: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

}