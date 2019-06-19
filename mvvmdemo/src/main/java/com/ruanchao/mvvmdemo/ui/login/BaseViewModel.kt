package com.ruanchao.mvvmdemo.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ruanchao.mvvmdemo.bean.BaseNetBean
import com.ruanchao.mvvmdemo.utils.schedule
import com.ruanchao.mvvmdemo.utils.set
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

open class BaseViewModel(private val repo: BaseRepo): ViewModel() {

    var collectErrInfo = MutableLiveData<String>().apply {
        value = null
    }

    fun collectArtical(id: Int){
        repo.collectArtical(id)
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
                }

            })

    }

    fun unCollectArtical(id: Int){
        repo.unCollectArtical(id)
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
                }

            })

    }

}