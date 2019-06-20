package com.ruanchao.mvvmdemo.ui.home

import androidx.lifecycle.MutableLiveData
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ruanchao.mvvmdemo.bean.*
import com.ruanchao.mvvmdemo.ui.collection.CollectionViewModel
import com.ruanchao.mvvmdemo.utils.set
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject


class HomeBlogViewModel(private val homeBlogRepo: HomeBlogRepo) : CollectionViewModel(homeBlogRepo) {

    private val _TAG: String = "HomeBlogViewModel"

    val gson: Gson by lazy {
        Gson()
    }

    private val _homeDataList = MutableLiveData<MutableList<HomeData>>().apply { value = mutableListOf() }
    val homeDataList: MutableLiveData<MutableList<HomeData>>
        get() = _homeDataList

    private val _refreshHomeDataList = MutableLiveData<MutableList<HomeData>>().apply { value = mutableListOf() }
    val refreshHomeDataList: MutableLiveData<MutableList<HomeData>>
        get() = _refreshHomeDataList

    private val _errorInfo = MutableLiveData<ErrorInfo>().apply {
        value = null
    }
    val errorInfo: MutableLiveData<ErrorInfo>
        get() = _errorInfo

    private val _isRefreshing = MutableLiveData<Boolean>().apply { value = false }
    val isRefreshing: MutableLiveData<Boolean>
        get() = _isRefreshing

    private val getBlogContentFromLocal: Observable<MutableList<HomeData>> = homeBlogRepo.getBlogContentFormLocal()
        .map {
            var newResMsg: MutableList<HomeData>? = mutableListOf()
            if (it != null && it.size > 0) {
                newResMsg = parseContent(it[0].content)
                Log.i(_TAG, "get from local:" + newResMsg.size)
            }
            newResMsg
        }

    fun getHomeData(page: Int) {
        val getBannerList: Observable<BaseNetBean<List<BannerInfo>>> = homeBlogRepo.getBannerListFormRemote()
        val getAllProjectByPage = homeBlogRepo.getAllProjectByPageFormRemote(page)

        //ZIP两次请求获取项目信息 和 Banner 信息，并进行合并数据源    每次请求必须要有Complete，不然不知道当前任务是否完成
        val getBlogContentFromRemote = Observable.zip(
            getAllProjectByPage,
            getBannerList,
            BiFunction<BaseNetBean<Projects>, BaseNetBean<List<BannerInfo>>, MutableList<HomeData>> { projectsBaseNetBean, listBaseNetBean ->
                val homeDatas: MutableList<HomeData> = mutableListOf()
                val projectList: List<DataInfo>? = projectsBaseNetBean.data?.datas
                projectList?.forEach { item ->
                    homeDatas.add(HomeData(HomeData.VIEW_TYPE_CONTENT, item))
                }

                val bannerLists: List<BannerInfo> = listBaseNetBean.data as List<BannerInfo>
                homeDatas.add(0, HomeData(HomeData.VIEW_TYPE_BANNER_LIST, bannerLists))
                Log.i(_TAG, "get from remote")
                homeDatas
            }).doOnNext {
            //进行网络请求数据的更新
            val content = gson.toJson(it)
            val blogContent = BlogContent(1, content)
            homeBlogRepo.addOrUpdateBlogContent(blogContent)
        }

        //concat 先从本地数据库拿缓存数据   然后再从网络拿数据   每次请求必须要有Complete，不然不知道当前任务是否完成
        Observable.concat(getBlogContentFromLocal, getBlogContentFromRemote)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread(), true)//  true这个参数非常非常重要，防止网络中断，提前进入onComplete,而没有执行onNext
            .doFinally {
                //在 RxJava 中 doFinally 和 doAfterTerminate 这两个操作符很类似，
                //都会在 Observable 的 onComplete 或 onError 调用之后进行调用。
                _isRefreshing.set(false)
            }
            .subscribe(
                object : Observer<MutableList<HomeData>> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(homeDatas: MutableList<HomeData>) {
                        Log.i(_TAG, "onNext")
                        if (homeDatas != null && homeDatas.size > 0) {
                            _homeDataList.set(homeDatas)
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.i(_TAG, "onError:" + e.localizedMessage)
                        errorInfo.set(ErrorInfo(ErrorInfo.ERROR_TYPE_LOAD, e.message))
                    }

                    override fun onComplete() {

                    }
                }
            )
    }

    fun getAllProjectByPage(page: Int) {
        homeBlogRepo.getAllProjectByPageFormRemote(page).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<BaseNetBean<Projects>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(data: BaseNetBean<Projects>) {
                    val homeDatas: MutableList<HomeData> = mutableListOf()
                    val projectList: List<DataInfo>? = data.data?.datas
                    projectList?.forEach { item ->
                        homeDatas.add(HomeData(HomeData.VIEW_TYPE_CONTENT, item))
                    }
                    _refreshHomeDataList.set(homeDatas)
                    _isRefreshing.set(false)
                }

                override fun onError(e: Throwable) {
                   errorInfo.set(ErrorInfo(ErrorInfo.ERROR_TYPE_REFRESH, e.message))
                    _isRefreshing.set(false)
                }

            })
    }

    private fun parseContent(content: String?): MutableList<HomeData> {

        val homeDataList: MutableList<HomeData> = mutableListOf()
        val jsonArray = JSONArray(content)
        for (i in 0..(jsonArray.length() - 1)) {

            val jsonValue: JSONObject = jsonArray.get(i) as JSONObject
            if (jsonValue["itemType"] == HomeData.VIEW_TYPE_BANNER_LIST) {
                val type = object : TypeToken<MutableList<BannerInfo>>() {}.type
                val bannerInfos = gson.fromJson<MutableList<BannerInfo>>(jsonValue["itemValue"].toString(), type)
                homeDataList.add(HomeData(HomeData.VIEW_TYPE_BANNER_LIST, bannerInfos))

            } else if (jsonValue["itemType"] == HomeData.VIEW_TYPE_CONTENT) {

                val projectInfo = gson.fromJson<DataInfo>(jsonValue["itemValue"].toString(), DataInfo::class.java)
                homeDataList.add(HomeData(HomeData.VIEW_TYPE_CONTENT, projectInfo))
            }
        }
        return homeDataList
    }

}