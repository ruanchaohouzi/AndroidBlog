package com.ruanchao.mvvmdemo.bean

class ErrorInfo(val errType:Int, val errMsg: String?){

    companion object {
        val ERROR_TYPE_LOAD = 1
        val ERROR_TYPE_REFRESH = 2
    }

}