package com.ruanchao.androidblog.ui.login

import com.ruanchao.androidblog.bean.User
import com.ruanchao.androidblog.db.UserDao
import com.ruanchao.androidblog.net.WanAndroidApi

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