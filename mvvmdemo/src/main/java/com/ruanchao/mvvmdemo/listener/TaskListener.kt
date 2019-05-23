package com.ruanchao.mvvmdemo.listener

import com.ruanchao.mvpframe.bean.HomeData

interface TaskListener{
    fun onTaskComplete(list: List<HomeData>, page:Int)
}