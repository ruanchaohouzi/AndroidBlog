package com.ruanchao.androidblog.extensions

import android.content.Context

fun Int.dp(context: Context): Float {
    return this * context.resources.displayMetrics.density
}