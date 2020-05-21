package com.ruanchao.androidblog

import android.app.Application
import android.content.Context
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.properties.Delegates

class MainApplication: Application() {

    companion object {
        var context: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("MainApplication","onCreate")

        
        context = this
    }

}