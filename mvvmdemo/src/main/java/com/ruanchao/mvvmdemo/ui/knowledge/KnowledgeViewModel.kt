package com.ruanchao.mvvmdemo.ui.knowledge

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ruanchao.mvvmdemo.bean.BaseNetBean
import com.ruanchao.mvvmdemo.bean.KnowledgeInfo
import com.ruanchao.mvvmdemo.bean.PublicNumerArticalInfo
import com.ruanchao.mvvmdemo.utils.schedule
import com.ruanchao.mvvmdemo.utils.set
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class KnowledgeViewModel(val remote: KnowledgeRepo): ViewModel() {

    var isKnowledgeChildDataListLoading = false

    var knowledgeChildError = MutableLiveData<String>().apply {
        value = null
    }
    var knowledgeChildDataInfo = MutableLiveData<PublicNumerArticalInfo>().apply {
        value = null
    }
    var isRefreshing = MutableLiveData<Boolean>()

    var knowledgeInfoList = MutableLiveData<List<KnowledgeInfo>>().apply {
        value = null
    }

    var errInfo = MutableLiveData<String>().apply {
        value = null
    }

    fun getKnowledgeList(){
        remote.getKnowledgeList()
            .schedule()
            .subscribe(object : Observer<BaseNetBean<List<KnowledgeInfo>>>{
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: BaseNetBean<List<KnowledgeInfo>>) {
                    knowledgeInfoList.set(t.data)
                }

                override fun onError(e: Throwable) {
                    errInfo.set(e.message)
                }

            })
    }

    fun getKnowledgeChildList(page: Int, cid:Int){
        remote.getKnowledgeChildList(page, cid)
            .schedule()
            .doOnSubscribe{
                //加载中
                isKnowledgeChildDataListLoading = true
                if (page == 0) {
                    isRefreshing.set(true)
                }
            }.doFinally {
                //加载完成
                isKnowledgeChildDataListLoading = false
                isRefreshing.set(false)
            }
            .subscribe(object : Observer<BaseNetBean<PublicNumerArticalInfo>>{
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: BaseNetBean<PublicNumerArticalInfo>) {
                    knowledgeChildDataInfo.set(t.data)
                }

                override fun onError(e: Throwable) {
                    knowledgeChildError.set(e.message)
                }

            })
    }
}