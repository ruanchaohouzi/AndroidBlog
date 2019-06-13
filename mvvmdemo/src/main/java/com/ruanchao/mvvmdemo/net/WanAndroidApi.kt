package com.ruanchao.mvvmdemo.net

import com.ruanchao.mvvmdemo.bean.*
import com.ruanchao.mvvmdemo.bean.KnowledgeInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

}