package com.ruanchao.androidblog.net

import com.ruanchao.androidblog.bean.*
import com.ruanchao.androidblog.bean.KnowledgeInfo
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
    fun getCollectList(@Path("page") page: Int):Observable<BaseNetBean<PublicNumerArticalInfo>>

    @POST("/lg/collect/{id}/json")
    fun collectArtical(@Path("id") id: Int):Observable<BaseNetBean<Any>>

    //收藏外部文章
    @FormUrlEncoded
    @POST("/lg/collect/add/json")
    fun collectExternalArtical(@Field("title") title:String,
                               @Field("author") author:String,
                               @Field("link") link:String):Observable<BaseNetBean<DataInfo>>

            @POST("/lg/uncollect_originId/{id}/json")
    fun unCollectArtical(@Path("id") id: Int):Observable<BaseNetBean<Any>>

    @FormUrlEncoded
    @POST("/lg/uncollect/{id}/json")
    fun unCollectMyArtical(@Path("id") id: Int, @Field("originId") originId:Int):Observable<BaseNetBean<Any>>

}