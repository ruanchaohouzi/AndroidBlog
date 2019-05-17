package com.ruanchao.mvpframe.Home

import com.ruanchao.mvpframe.basemvp.baseview.IBaseView
import com.ruanchao.mvpframe.bean.BannerInfo
import com.ruanchao.mvpframe.bean.BaseNetBean
import com.ruanchao.mvpframe.bean.HomeData
import com.ruanchao.mvpframe.bean.Projects

interface IHomeView: IBaseView{

    fun onGetProjectsSuccess(data: MutableList<HomeData>?, page: Int)
    fun onGetProjectsFail(errMsg: String?)

    fun onGetBannerListSuccess(data: BaseNetBean<List<BannerInfo>>?)
    fun onGetBannerListFail(errMsg: String?)

    fun onGetHomeDataSuccess(data: MutableList<HomeData>?, page: Int)
    fun onGetHomeDataFail(errMsg: String?)

}