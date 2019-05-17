package com.ruanchao.mvpframe.Home

import com.ruanchao.mvpframe.bean.BannerInfo
import com.ruanchao.mvpframe.bean.BaseNetBean
import com.ruanchao.mvpframe.bean.Projects
import com.ruanchao.mvpframe.net.NetWorkManager
import rx.Observable

class HomeModel{

    fun getAllProjectByPage(page: Int): Observable<BaseNetBean<Projects>> {
        return NetWorkManager.getInstance().getRequestApi().getAllProjectByPage(page)
    }

    fun getBannerList(): Observable<BaseNetBean<List<BannerInfo>>> {
        return NetWorkManager.getInstance().getRequestApi().getBannerList()
    }

}