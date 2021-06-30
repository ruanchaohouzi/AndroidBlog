package com.ruanchao.androidblog.ui.login2

import com.ruanchao.androidblog.bean.User
import com.ruanchao.androidblog.db.UserDao
import com.ruanchao.androidblog.net.WanAndroidApi
import com.ruanchao.androidblog.net2.LoginApi

class LoginRepo(private val remote: LoginApi, private val local:UserDao?){

    suspend fun register(username: String,
                 password: String,
                 repassword: String)
    = remote.register(username, password, repassword)

    suspend fun login(username: String, password: String) = remote.login(username, password)

    suspend fun logout() = remote.logout()

    suspend fun insert(user: User) = local?.insert(user)

    suspend fun getUserInfo() = local?.getUserInfo()



}