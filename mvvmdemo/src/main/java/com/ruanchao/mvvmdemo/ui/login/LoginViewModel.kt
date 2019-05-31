package com.ruanchao.mvvmdemo.ui.login

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import android.util.Log
import com.ruanchao.mvvmdemo.bean.UserInfo1
import com.ruanchao.mvvmdemo.utils.schedule
import com.ruanchao.mvvmdemo.utils.set
import io.reactivex.*
import io.reactivex.disposables.Disposable


class LoginViewModel(private val repo: LoginRepo): ViewModel() {

    var userName: MutableLiveData<String> = MutableLiveData()
    var pwd: MutableLiveData<String> = MutableLiveData()
    var userInfoData: MutableLiveData<UserInfo1> = MutableLiveData()
    var error: MutableLiveData<String> = MutableLiveData()

    fun loginOrRegist(){
        if (TextUtils.isEmpty(userName.value) || TextUtils.isEmpty(pwd.value)){
            error.set("用户名或者密码不能为空")
            return
        }
        val userInfo = UserInfo1(userName.value!!, pwd.value!!);
        Observable.create(ObservableOnSubscribe<UserInfo1?> {
            var newUserInfo = repo.login(userInfo)
            if (newUserInfo == null) {
                //进入FlatMap中进行处理
                it.onNext(userInfo)
            } else {
                it.onNext(newUserInfo)
            }
        }).flatMap { t ->
            //先注册
            if (t.userId == 0) {
                Log.i("LoginViewModel", " not logined")
                repo.regist(t!!)
                Log.i("LoginViewModel", " regist")
                //在登录
                Observable.create(ObservableOnSubscribe<UserInfo1> {
                    var user: UserInfo1? = repo.login(t)
                    //进入Observer中进行处理
                    it.onNext(user!!)
                }).schedule()
            }else {
                Log.i("LoginViewModel", "logined")
                Observable.just(t)
            }
        }.schedule()
            .subscribe(object : Observer<UserInfo1?> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: UserInfo1) {
                    userInfoData.set(t)
                    Log.i("LoginViewModel", "onNext:${t.userId}")
                }

                override fun onError(e: Throwable) {
                    error.set(e.message)
                    Log.i("LoginViewModel", "onError:${e.message}")
                }

            })
    }

}