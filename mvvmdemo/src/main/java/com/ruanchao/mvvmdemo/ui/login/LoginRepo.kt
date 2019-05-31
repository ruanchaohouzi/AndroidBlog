package com.ruanchao.mvvmdemo.ui.login

import com.ruanchao.mvvmdemo.bean.LoginRequestModel
import com.ruanchao.mvvmdemo.bean.UserInfo1
import com.ruanchao.mvvmdemo.db.UserDao
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body

class LoginRepo(private val local: UserDao){

    fun login(userInfo1: UserInfo1): UserInfo1?
    = local.getUserInfo(userInfo1.userName!!, userInfo1.pwd!!)

    fun regist(userInfo1: UserInfo1) = local.insert(userInfo1)

}