package com.ruanchao.mvvmdemo.ui.login

import android.arch.lifecycle.ViewModel
import com.ruanchao.mvvmdemo.bean.UserAccessToken
import com.ruanchao.mvvmdemo.utils.schedule
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class LoginViewModel(private val repo: LoginRepo): ViewModel() {

    fun login(){
        var userName: String = ""
        var pwd:String = ""
        repo.authorizations(userName, pwd)
            .schedule()
            .subscribe(object: Observer<UserAccessToken> {

                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(data: UserAccessToken) {
                    print(data.token)
                }

                override fun onError(e: Throwable) {
                    print(e.message)
                }
            })
    }

}