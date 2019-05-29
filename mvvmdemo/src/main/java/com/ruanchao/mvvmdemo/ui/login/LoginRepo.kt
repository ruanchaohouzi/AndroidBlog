package com.ruanchao.mvvmdemo.ui.login

import com.ruanchao.mvvmdemo.bean.LoginRequestModel
import com.ruanchao.mvvmdemo.bean.UserAccessToken
import com.ruanchao.mvvmdemo.net.GithubApi
import io.reactivex.Observable
import retrofit2.http.Body

class LoginRepo(private val remote: GithubApi){

    fun authorizations(userName: String, pwd:String): Observable<UserAccessToken> {

        var authRequestModel: LoginRequestModel = LoginRequestModel.generate()
        return remote.authorizations(authRequestModel)
    }
}