package com.ruanchao.mvvmdemo.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.ruanchao.mvpframe.bean.*
import com.ruanchao.mvpframe.net.RequestApi
import com.ruanchao.mvvmdemo.utils.set
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription


class HomeBlogViewModel(private val remote: RequestApi): ViewModel() {

    private val _homeDataList = MutableLiveData<MutableList<HomeData>>().apply { value = mutableListOf() }
    val homeDataList: MutableLiveData<MutableList<HomeData>>
        get() = _homeDataList

    private val _refreshHomeDataList = MutableLiveData<MutableList<HomeData>>().apply { value = mutableListOf() }
    val refreshHomeDataList: MutableLiveData<MutableList<HomeData>>
        get() = _refreshHomeDataList

    fun getHomeData(page: Int){
        val getBannerList: Observable<BaseNetBean<List<BannerInfo>>> = remote.getBannerList().observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
        val getAllProjectByPage = remote.getAllProjectByPage(page).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

        Observable.zip(
            getAllProjectByPage,
            getBannerList,
            BiFunction<BaseNetBean<Projects>, BaseNetBean<List<BannerInfo>>, MutableList<HomeData>> { projectsBaseNetBean, listBaseNetBean ->
                var homeDatas: MutableList<HomeData> = mutableListOf()
                val projectList: List<ProjectInfo>? = projectsBaseNetBean.data?.datas
                projectList?.forEach{
                        item->homeDatas.add(HomeData(HomeData.VIEW_TYPE_CONTENT,item))
                }

                var bannerLists: List<BannerInfo> = listBaseNetBean.data as List<BannerInfo>
                homeDatas.add(0, HomeData(HomeData.VIEW_TYPE_BANNER_LIST,bannerLists))
                 homeDatas
            })
            .subscribe(
                object : Observer<MutableList<HomeData>> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(homeData: MutableList<HomeData>) {
//                        onGetHomeDataSuccess(homeDatas, page)
                        _homeDataList.set(homeData)
                    }

                    override fun onError(e: Throwable) {

                        print(e.message)
                    }

                    override fun onComplete() {

                    }
                }
            )
    }

    fun getAllProjectByPage(page: Int){
            remote!!.getAllProjectByPage(page).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<BaseNetBean<Projects>>{
                    override fun onComplete() {
                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(data: BaseNetBean<Projects>) {
                        var homeDatas: MutableList<HomeData> = mutableListOf()
                        val projectList: List<ProjectInfo>? = data?.data?.datas
                        projectList?.forEach{
                                item->homeDatas.add(HomeData(HomeData.VIEW_TYPE_CONTENT,item))
                        }
                        _refreshHomeDataList.set(homeDatas)
                    }

                    override fun onError(e: Throwable) {
                    }

                })

    }

}