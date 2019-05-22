package com.ruanchao.mvpframe.bean

class HomeData(var itemType: Int, var itemValue: Any?){
    var type: Int = -1
    var item: Any? = null

    init {
        type = itemType
        item = itemValue
    }

    companion object {

        val VIEW_TYPE_BANNER_LIST = 0
        val VIEW_TYPE_CONTENT = 1
        val VIEW_TYPE_FOOT= 2
    }
}