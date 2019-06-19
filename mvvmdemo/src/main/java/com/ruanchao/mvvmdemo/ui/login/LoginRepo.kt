package com.ruanchao.mvvmdemo.ui.login

import com.ruanchao.mvvmdemo.bean.BaseNetBean
import com.ruanchao.mvvmdemo.bean.LoginRequestModel
import com.ruanchao.mvvmdemo.bean.User
import com.ruanchao.mvvmdemo.db.UserDao
import com.ruanchao.mvvmdemo.net.WanAndroidApi
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body

class LoginRepo(private val remote: WanAndroidApi, private val local:UserDao?){

    fun register(username: String,
                 password: String,
                 repassword: String)
    = remote.register(username, password, repassword)

    fun login(username: String, password: String) = remote.login(username, password)

    fun logout() = remote.logout()

    fun insert(user: User) = local?.insert(user)

    fun getUserInfo() = local?.getUserInfo()



}