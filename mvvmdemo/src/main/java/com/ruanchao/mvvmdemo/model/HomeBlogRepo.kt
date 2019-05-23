package com.ruanchao.mvvmdemo.model

import com.ruanchao.mvpframe.bean.BannerInfo
import com.ruanchao.mvpframe.bean.BaseNetBean
import com.ruanchao.mvpframe.bean.Projects
import com.ruanchao.mvpframe.net.NetWorkManager
import com.ruanchao.mvpframe.net.RequestApi
import io.reactivex.Observable

class HomeBlogRepo(private val remote: RequestApi){


    fun getAllProjectByPage(page: Int): Observable<BaseNetBean<Projects>> {
        return remote.getAllProjectByPage(page)
    }

    fun getBannerList(): Observable<BaseNetBean<List<BannerInfo>>> {
        return remote.getBannerList()
    }

}