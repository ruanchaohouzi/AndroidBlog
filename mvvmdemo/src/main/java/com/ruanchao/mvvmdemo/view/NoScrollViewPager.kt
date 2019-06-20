package com.ruanchao.mvvmdemo.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NoScrollViewPager(var mContext: Context, var attrs: AttributeSet? = null): ViewPager(mContext, attrs) {

    // false 禁止ViewPager左右滑动。
    val scrollable = false

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return scrollable
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return scrollable
    }

}