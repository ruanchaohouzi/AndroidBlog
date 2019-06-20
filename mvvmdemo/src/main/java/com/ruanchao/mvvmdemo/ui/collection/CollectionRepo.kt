package com.ruanchao.mvvmdemo.ui.collection

import com.ruanchao.mvvmdemo.bean.BaseNetBean
import com.ruanchao.mvvmdemo.net.WanAndroidApi
import io.reactivex.Observable
import retrofit2.http.Field


open class CollectionRepo(private val remote: WanAndroidApi){
    fun collectArtical(id: Int) = remote.collectArtical(id)

    fun unCollectArtical(id: Int) = remote.unCollectArtical(id)

    fun getCollectList(page: Int) = remote.getCollectList(page)

    fun unCollectMyArtical(id: Int,originId:Int) = remote.unCollectMyArtical(id, originId)

    fun collectExternalArtical(
        title: String,
        author: String,
        link: String
    ) = remote.collectExternalArtical(title, author, link)


}