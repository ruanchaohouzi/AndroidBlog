package com.ruanchao.mvvmdemo.net

import com.ruanchao.mvvmdemo.bean.*
import com.ruanchao.mvvmdemo.bean.KnowledgeInfo
import io.reactivex.Observable
import retrofit2.http.*

interface WanAndroidApi{

    @GET("/article/listproject/{page}/json")
    fun getAllProjectByPage(@Path("page") page: Int): Observable<BaseNetBean<Projects>>

    @GET("/banner/json")
    fun getBannerList(): Observable<BaseNetBean<List<BannerInfo>>>

    @GET("http://baobab.kaiyanapp.com/api/v2/feed?{page}")
    fun getVideoListBean(@Path("page") page: Int): Observable<VideoListBean>

    @GET("/wxarticle/chapters/json")
    fun getPublicNumberList(): Observable<BaseNetBean<List<PublicNumberInfo>>>

    @GET("/wxarticle/list/{id}/{page}/json")
    fun getPublicNumberDataList(@Path("id") id: Int, @Path("page") page:Int): Observable<BaseNetBean<PublicNumerArticalInfo>>

    @GET("/tree/json")
    fun getKnowledgeList():Observable<BaseNetBean<List<KnowledgeInfo>>>

    @GET("/article/list/{page}/json")
    fun getKnowledgeChildList(@Path("page") page: Int, @Query("cid") cid:Int):Observable<BaseNetBean<PublicNumerArticalInfo>>

    @FormUrlEncoded
    @POST("/user/register")
    fun register(@Field("username") username: String,
                 @Field("password") password: String,
                 @Field("repassword") repassword: String):Observable<BaseNetBean<User>>

    @FormUrlEncoded
    @POST("/user/login")
    fun login(@Field("username") username: String,
              @Field("password") password: String):Observable<BaseNetBean<User>>

    @GET("/user/logout/json")
    fun logout():Observable<BaseNetBean<Any>>

    @GET("/lg/collect/list/{page}/json")
    fun getCollectList(@Path("page") page: Int):Observable<BaseNetBean<CollectInfo>>

    @POST("/lg/collect/{id}/json")
    fun collectArtical(@Path("id") id: Int):Observable<BaseNetBean<Any>>

    @POST("lg/uncollect_originId/{id}/json")
    fun unCollectArtical(@Path("id") id: Int):Observable<BaseNetBean<Any>>






}