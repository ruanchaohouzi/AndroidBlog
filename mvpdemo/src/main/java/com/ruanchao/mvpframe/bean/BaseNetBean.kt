package com.ruanchao.mvpframe.bean

import com.ruanchao.mvpframe.basemvp.basemodel.IModel

class BaseNetBean<T> {

    var errorCode : Int = -1

    var errorMsg: String? = null

    var data: T? = null
}