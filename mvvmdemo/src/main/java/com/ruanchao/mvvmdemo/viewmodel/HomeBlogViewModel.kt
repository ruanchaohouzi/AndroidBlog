package com.ruanchao.mvvmdemo.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.ruanchao.mvpframe.bean.*
import com.ruanchao.mvvmdemo.bean.BlogContent
import com.ruanchao.mvvmdemo.model.HomeBlogRepo
import com.ruanchao.mvvmdemo.utils.schedule
import com.ruanchao.mvvmdemo.utils.set
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Type


class HomeBlogViewModel(private val homeBlogRepo: HomeBlogRepo) : ViewModel() {

    private val TAG: String = "HomeBlogViewModel"

    val gson: Gson by lazy {
        Gson()
    }

    private val _homeDataList = MutableLiveData<MutableList<HomeData>>().apply { value = mutableListOf() }
    val homeDataList: MutableLiveData<MutableList<HomeData>>
        get() = _homeDataList

    private val _refreshHomeDataList = MutableLiveData<MutableList<HomeData>>().apply { value = mutableListOf() }
    val refreshHomeDataList: MutableLiveData<MutableList<HomeData>>
        get() = _refreshHomeDataList

    private val _errorInfo = MutableLiveData<String>()
    val errorInfo: MutableLiveData<String>
        get() = _errorInfo

    val getBlogContentFromLocal: Observable<MutableList<HomeData>> = homeBlogRepo.getBlogContentFormLocal()
        .map {
            var newResMsg: MutableList<HomeData>? = mutableListOf()
            if (it != null && it.size > 0) {
                newResMsg = parseContent(it[0].content)
                Log.i(TAG, "get from local")
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
                var homeDatas: MutableList<HomeData> = mutableListOf()
                val projectList: List<ProjectInfo>? = projectsBaseNetBean.data?.datas
                projectList?.forEach { item ->
                    homeDatas.add(HomeData(HomeData.VIEW_TYPE_CONTENT, item))
                }

                var bannerLists: List<BannerInfo> = listBaseNetBean.data as List<BannerInfo>
                homeDatas.add(0, HomeData(HomeData.VIEW_TYPE_BANNER_LIST, bannerLists))
                Log.i(TAG, "get from remote")
                homeDatas
            }).doOnNext {
            //进行网络请求数据的更新
            val content = gson.toJson(it)
            var blogContent: BlogContent = BlogContent(1, content)
            homeBlogRepo.addOrUpdateBlogContent(blogContent)
        }

        //concat 先从本地数据库拿缓存数据   然后再从网络拿数据   每次请求必须要有Complete，不然不知道当前任务是否完成
        Observable.concat(getBlogContentFromLocal, getBlogContentFromRemote)
            .schedule()
            .subscribe(
                object : Observer<MutableList<HomeData>> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onNext(homeDatas: MutableList<HomeData>) {
                        Log.i(TAG, "onNext:${gson.toJson(homeDatas)}")
                        if (homeDatas != null && homeDatas.size > 0) {
                            _homeDataList.set(homeDatas)
                        }
                    }

                    override fun onError(e: Throwable) {
                        errorInfo.set(e.message)
                        print(e.message)
                    }

                    override fun onComplete() {

                    }
                }
            )
    }

    fun getAllProjectByPage(page: Int) {
        homeBlogRepo!!.getAllProjectByPageFormRemote(page).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : Observer<BaseNetBean<Projects>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(data: BaseNetBean<Projects>) {
                    var homeDatas: MutableList<HomeData> = mutableListOf()
                    val projectList: List<ProjectInfo>? = data?.data?.datas
                    projectList?.forEach { item ->
                        homeDatas.add(HomeData(HomeData.VIEW_TYPE_CONTENT, item))
                    }
                    _refreshHomeDataList.set(homeDatas)
                }

                override fun onError(e: Throwable) {
                    errorInfo.set(e.message)
                }

            })
    }

    fun parseContent(content: String?): MutableList<HomeData> {

        var homeDataList: MutableList<HomeData> = mutableListOf()
        val jsonArray: JSONArray = JSONArray(content)
        for (i in 0..(jsonArray.length() - 1)) {

            val jsonValue: JSONObject = jsonArray.get(i) as JSONObject
            if (jsonValue["itemType"] == HomeData.VIEW_TYPE_BANNER_LIST) {
                val type = object : TypeToken<MutableList<BannerInfo>>() {}.type
                val bannerInfos = gson.fromJson<MutableList<BannerInfo>>(jsonValue["itemValue"].toString(), type)
                homeDataList.add(HomeData(HomeData.VIEW_TYPE_BANNER_LIST, bannerInfos))

            } else if (jsonValue["itemType"] == HomeData.VIEW_TYPE_CONTENT) {

                val projectInfo = gson.fromJson<ProjectInfo>(jsonValue["itemValue"].toString(), ProjectInfo::class.java)
                homeDataList.add(HomeData(HomeData.VIEW_TYPE_CONTENT, projectInfo))
            }
        }
        return homeDataList
    }

}