package com.ruanchao.mvpframe.Home

import com.ruanchao.mvpframe.bean.BannerInfo
import com.ruanchao.mvpframe.bean.BaseNetBean
import com.ruanchao.mvpframe.bean.HomeData
import com.ruanchao.mvpframe.bean.Projects
import rx.Observable
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func2
import rx.schedulers.Schedulers

class test {

    fun test2(page: Int) {
        val mHomeModel = HomeModel()
        val a1 = mHomeModel.getAllProjectByPage(page).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
        val a2 = mHomeModel.getBannerList().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
        Observable.zip(a1, a2) { projectsBaseNetBean, listBaseNetBean ->
            val homeData = HomeData(1, null)
            homeData.itemType = 2
            homeData.itemValue = "dadsda"
            homeData
        }.subscribe(object : Observer<HomeData> {
            override fun onCompleted() {
                println("onCompleted")
            }

            override fun onError(e: Throwable) {

                println("e")
            }

            override fun onNext(homeData: HomeData) {
                println("homeData")
            }
        })

    }
}
