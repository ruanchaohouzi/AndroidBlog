package com.ruanchao.androidblog.ui.collection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ruanchao.androidblog.bean.BaseNetBean
import com.ruanchao.androidblog.bean.DataInfo
import com.ruanchao.androidblog.bean.PublicNumerArticalInfo
import com.ruanchao.androidblog.utils.isHttpUrl
import com.ruanchao.androidblog.utils.schedule
import com.ruanchao.androidblog.utils.set
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

open class CollectionViewModel(private val repo: CollectionRepo): ViewModel() {

    var isGetCollectionListLoading = false

    var collectErrInfo = MutableLiveData<String>().apply {
        value = null
    }

    var collectListErrInfo = MutableLiveData<Int>().apply {
        value = null
    }

    var dataInfos = MutableLiveData<List<DataInfo>>().apply {
        value = null
    }

    var isCollectionRefreshing = MutableLiveData<Boolean>()

    var title = MutableLiveData<String>().apply {
        value = null
    }

    var author = MutableLiveData<String>().apply {
        value = null
    }

    var link = MutableLiveData<String>().apply {
        value = null
    }

    var addCollectionInfo = MutableLiveData<DataInfo>().apply {
        value = null
    }

    var unCollectInfo = MutableLiveData<DataInfo>().apply {
        value = null
    }

    fun collectArtical(dataInfo: DataInfo){
        repo.collectArtical(dataInfo.id)
            .schedule()
            .subscribe(object : Observer<BaseNetBean<Any>>{
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: BaseNetBean<Any>) {
                }

                override fun onError(e: Throwable) {
                    collectErrInfo.set(e.message)
                    dataInfo.collect = false
                }

            })

    }

    fun unCollectArtical(dataInfo: DataInfo){
        repo.unCollectArtical(dataInfo.id)
            .schedule()
            .subscribe(object : Observer<BaseNetBean<Any>>{
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: BaseNetBean<Any>) {
                    unCollectInfo.set(dataInfo)
                }

                override fun onError(e: Throwable) {
                    dataInfo.collect = true
                    collectErrInfo.set(e.message)
                }

            })
    }

    fun unCollectMyArtical(dataInfo: DataInfo){

        var originId = if (dataInfo.originId == 0) -1 else dataInfo.originId
        repo.unCollectMyArtical(dataInfo.id, originId)
            .schedule()
            .subscribe(object : Observer<BaseNetBean<Any>>{
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: BaseNetBean<Any>) {
                    unCollectInfo.set(dataInfo)
                }

                override fun onError(e: Throwable) {
                    dataInfo.collect = true
                    collectErrInfo.set(e.message)
                }

            })
    }

    fun getCollectList(page: Int){
        repo.getCollectList(page)
            .schedule()
            .subscribe(object: Observer<BaseNetBean<PublicNumerArticalInfo>>{
                override fun onComplete() {
                    isCollectionRefreshing.set(false)
                    isGetCollectionListLoading = false
                }

                override fun onSubscribe(d: Disposable) {
                    isGetCollectionListLoading = true
                    if (page == 0) {
                        isCollectionRefreshing.set(true)
                    }
                }

                override fun onNext(t: BaseNetBean<PublicNumerArticalInfo>) {
                    if (t.errorCode == 0) {
                        dataInfos.set(t.data?.datas)
                    } else {
                        collectListErrInfo.set(t.errorCode)
                    }
                }

                override fun onError(e: Throwable) {
                    collectListErrInfo.set(-10000)
                }

            })
    }

    fun collectExternalArtical(){

        if (title.value.isNullOrEmpty()
            || author.value.isNullOrEmpty()
            || link.value.isNullOrEmpty()){
            collectErrInfo.set("填写信息不可以为空")
            return
        }else if (!(link.value as String).isHttpUrl()){
            collectErrInfo.set("请填写正确的url")
            return
        }

        repo.collectExternalArtical(title.value!!, author.value!!, link.value!!)
            .schedule()
            .subscribe(object : Observer<BaseNetBean<DataInfo>>{
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: BaseNetBean<DataInfo>) {
                    if (t.errorCode == 0) {
                        addCollectionInfo.value = t.data
                        //清空输入框数据
                        title.value = null
                        author.value = null
                        link.value = null
                    }else{
                        collectErrInfo.set(t.errorMsg)
                    }
                }

                override fun onError(e: Throwable) {
                    collectErrInfo.set(e.message)
                }

            })
    }

}