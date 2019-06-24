package com.ruanchao.androidblog.bean

class HomeData(var itemType: Int = -1, var itemValue: Any?){

    companion object {

        const val VIEW_TYPE_BANNER_LIST = 0
        const val VIEW_TYPE_CONTENT = 1
        const val VIEW_TYPE_FOOT= 2
    }
}