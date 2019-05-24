package com.ruanchao.mvpframe.bean

class HomeData(var itemType: Int = -1, var itemValue: Any?){

    companion object {

        val VIEW_TYPE_BANNER_LIST = 0
        val VIEW_TYPE_CONTENT = 1
        val VIEW_TYPE_FOOT= 2
    }
}