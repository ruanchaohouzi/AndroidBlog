package com.ruanchao.mvvmdemo.ui.publicnumber

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.ruanchao.mvvmdemo.bean.BaseNetBean
import com.ruanchao.mvvmdemo.bean.PublicNumberInfo
import com.ruanchao.mvvmdemo.bean.PublicNumerArticalInfo
import com.ruanchao.mvvmdemo.utils.schedule
import com.ruanchao.mvvmdemo.utils.set
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class PublicNumberViewModel(val repo: PublicNumberRepo): ViewModel(){

    var publicNumberInfos = MutableLiveData<List<PublicNumberInfo>>()
    var error = MutableLiveData<String>()
    var publicNumerArticalInfo = MutableLiveData<PublicNumerArticalInfo>()
    var isRefreshing = MutableLiveData<Boolean>()
    var isPublicNumberDataListLoading = false

    fun getPublicNumberList(){
        repo.getPublicNumberList()
            .schedule()
            .subscribe(object : Observer<BaseNetBean<List<PublicNumberInfo>>>{
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: BaseNetBean<List<PublicNumberInfo>>) {
                    publicNumberInfos.set(t.data)
                }

                override fun onError(e: Throwable) {
                    error.set(e.message)
                }

            })
    }

    fun getPublicNumberDataList(id: Int, page:Int){
        Log.i("mCurrentId", "getPublicNumberDataList:" + id)
        repo.getPublicNumberDataList(id, page)
            .doOnSubscribe{
                //加载中
                isPublicNumberDataListLoading = true
                if (page == 0) {
                    isRefreshing.set(true)
                }
            }.doFinally {
                //加载完成
                isPublicNumberDataListLoading = false
                isRefreshing.set(false)
            }.schedule()
            .subscribe(object : Observer<BaseNetBean<PublicNumerArticalInfo>>{
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: BaseNetBean<PublicNumerArticalInfo>) {
                    publicNumerArticalInfo.set(t.data)
                }

                override fun onError(e: Throwable) {
                    error.set(e.message)
                }

            })
    }

}