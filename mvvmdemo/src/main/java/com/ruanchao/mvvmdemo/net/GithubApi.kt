package com.ruanchao.mvvmdemo.net

import com.ruanchao.mvvmdemo.bean.LoginRequestModel
import com.ruanchao.mvvmdemo.bean.UserAccessToken
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GithubApi {

    @POST("authorizations")
    @Headers("Accept: application/json")
    fun authorizations(
            @Body authRequestModel: LoginRequestModel
    ): Observable<UserAccessToken>
}