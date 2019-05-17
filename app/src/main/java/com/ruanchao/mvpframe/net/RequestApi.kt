package com.ruanchao.mvpframe.net

import com.ruanchao.mvpframe.bean.BannerInfo
import com.ruanchao.mvpframe.bean.BaseNetBean
import com.ruanchao.mvpframe.bean.Projects
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface RequestApi{

    //TODO 有问题

    @GET("/article/listproject/{page}/json")
    fun getAllProjectByPage(@Path("page") page: Int): Observable<BaseNetBean<Projects>>

    @GET("/banner/json")
    fun getBannerList(): Observable<BaseNetBean<List<BannerInfo>>>
}