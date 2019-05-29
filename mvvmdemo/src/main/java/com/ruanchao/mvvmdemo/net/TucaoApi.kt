package com.ruanchao.mvvmdemo.net

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers

interface TucaoApi{

    @GET("/")
    @Headers("Cookie: tucao_verify=ok")
    fun index() : Observable<ResponseBody>
}