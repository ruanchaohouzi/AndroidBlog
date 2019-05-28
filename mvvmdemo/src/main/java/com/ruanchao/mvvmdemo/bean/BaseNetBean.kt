package com.ruanchao.mvvmdemo.bean

class BaseNetBean<T> {

    var errorCode : Int = -1

    var errorMsg: String? = null

    var data: T? = null
}