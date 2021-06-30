package com.ruanchao.androidblog.net2

import com.ruanchao.androidblog.bean.BaseNetBean
import com.ruanchao.androidblog.bean.User
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginApi{

    @FormUrlEncoded
    @POST("/user/register")
    suspend fun register(@Field("username") username: String,
                 @Field("password") password: String,
                 @Field("repassword") repassword: String): BaseNetBean<User>

    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(@Field("username") username: String,
              @Field("password") password: String): BaseNetBean<User>

    @GET("/user/logout/json")
    suspend fun logout(): BaseNetBean<Any>
}