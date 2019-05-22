package com.ruanchao.mvpframe.net

import com.ruanchao.mvpframe.bean.BannerInfo
import com.ruanchao.mvpframe.bean.BaseNetBean
import com.ruanchao.mvpframe.bean.Projects
import com.ruanchao.mvpframe.bean.VideoListBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface RequestApi{

    @GET("/article/listproject/{page}/json")
    fun getAllProjectByPage(@Path("page") page: Int): Observable<BaseNetBean<Projects>>

    @GET("/banner/json")
    fun getBannerList(): Observable<BaseNetBean<List<BannerInfo>>>

    @GET("http://baobab.kaiyanapp.com/api/v2/feed?{page}")
    fun getVideoListBean(@Path("page") page: Int): Observable<VideoListBean>
}