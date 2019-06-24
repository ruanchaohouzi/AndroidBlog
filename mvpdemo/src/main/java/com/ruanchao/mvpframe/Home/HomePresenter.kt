package com.ruanchao.mvpframe.Home

import com.ruanchao.mvpframe.basemvp.basepresenter.BasePresenter
import com.ruanchao.mvpframe.bean.*
import rx.Observable
import rx.Observer
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func2
import rx.schedulers.Schedulers
import java.util.function.BiFunction

class HomePresenter: BasePresenter<IHomeView>() {

    var mHomeModel: HomeModel? = null

    init {
        mHomeModel = HomeModel()
    }

    fun getAllProjectByPage(page: Int){
        mCompositeSubscription.add(
            mHomeModel!!.getAllProjectByPage(page).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    object : Subscriber<BaseNetBean<Projects>>() {
                        override fun onError(e: Throwable?) {
                            viewRef?.get()?.onGetProjectsFail(e?.message)
                        }

                        override fun onCompleted() {
                        }

                        override fun onNext(data: BaseNetBean<Projects>?) {
                            var homeDatas: MutableList<HomeData> = mutableListOf()
                            val projectList: List<ProjectInfo>? = data?.data?.datas
                            projectList?.forEach{
                                    item->homeDatas.add(HomeData(HomeData.VIEW_TYPE_CONTENT,item))
                            }
                            viewRef?.get()?.onGetProjectsSuccess(homeDatas, page)
                            onCompleted()
                        }
                    }
                )
        )
    }

    fun getBannerList(){
        mCompositeSubscription.add(
            mHomeModel!!.getBannerList().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    object : Subscriber<BaseNetBean<List<BannerInfo>>>() {
                        override fun onError(e: Throwable?) {
                            viewRef?.get()?.onGetBannerListFail(e?.message)
                        }

                        override fun onCompleted() {
                        }

                        override fun onNext(data: BaseNetBean<List<BannerInfo>>?) {
                            viewRef?.get()?.onGetBannerListSuccess(data)
                            onCompleted()
                        }
                    }
                )
        )
    }

    fun getHomeData(page: Int){
        val getBannerList = mHomeModel!!.getBannerList().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
        val getAllProjectByPage = mHomeModel!!.getAllProjectByPage(page).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

        Observable.zip(getAllProjectByPage,getBannerList,{projects: BaseNetBean<Projects>, banners: BaseNetBean<List<BannerInfo>> ->

            var homeDatas: MutableList<HomeData> = mutableListOf()
            val projectList: List<ProjectInfo>? = projects.data?.datas
            projectList?.forEach{
                    item->homeDatas.add(HomeData(HomeData.VIEW_TYPE_CONTENT,item))
            }

            var bannerLists: List<BannerInfo> = banners.data as List<BannerInfo>
            homeDatas.add(0,HomeData(HomeData.VIEW_TYPE_BANNER_LIST,bannerLists))
            homeDatas
        }).subscribe(object : Observer<MutableList<HomeData>> {
            override fun onCompleted() {
                println("onCompleted")
            }

            override fun onError(e: Throwable) {
                viewRef?.get()?.onGetHomeDataFail(e?.message)
                println("e")
            }

            override fun onNext(homeDatas: MutableList<HomeData>) {
                println("homeData")
                viewRef?.get()?.onGetHomeDataSuccess(homeDatas, page)
                onCompleted()
            }
        })

    }






}