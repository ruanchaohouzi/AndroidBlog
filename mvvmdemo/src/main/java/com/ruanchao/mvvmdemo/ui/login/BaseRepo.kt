package com.ruanchao.mvvmdemo.ui.login

import com.ruanchao.mvvmdemo.bean.BaseNetBean
import com.ruanchao.mvvmdemo.bean.LoginRequestModel
import com.ruanchao.mvvmdemo.bean.User
import com.ruanchao.mvvmdemo.db.UserDao
import com.ruanchao.mvvmdemo.net.WanAndroidApi
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Path

open class BaseRepo(private val remote: WanAndroidApi){
    fun collectArtical(id: Int) = remote.collectArtical(id)

    fun unCollectArtical(id: Int) = remote.unCollectArtical(id)
}