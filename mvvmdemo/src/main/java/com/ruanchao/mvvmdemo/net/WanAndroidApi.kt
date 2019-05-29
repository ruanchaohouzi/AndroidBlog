package com.ruanchao.mvvmdemo.net

import com.ruanchao.mvvmdemo.bean.BannerInfo
import com.ruanchao.mvvmdemo.bean.BaseNetBean
import com.ruanchao.mvvmdemo.bean.Projects
import com.ruanchao.mvvmdemo.bean.VideoListBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface WanAndroidApi{

    @GET("/article/listproject/{page}/json")
    fun getAllProjectByPage(@Path("page") page: Int): Observable<BaseNetBean<Projects>>

    @GET("/banner/json")
    fun getBannerList(): Observable<BaseNetBean<List<BannerInfo>>>

    @GET("http://baobab.kaiyanapp.com/api/v2/feed?{page}")
    fun getVideoListBean(@Path("page") page: Int): Observable<VideoListBean>
}