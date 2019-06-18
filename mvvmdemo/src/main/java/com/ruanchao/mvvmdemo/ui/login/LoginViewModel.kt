package com.ruanchao.mvvmdemo.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.text.TextUtils
import android.util.Log
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import com.ruanchao.mvvmdemo.bean.BaseNetBean
import com.ruanchao.mvvmdemo.bean.User
import com.ruanchao.mvvmdemo.bean.UserInfo1
import com.ruanchao.mvvmdemo.utils.schedule
import com.ruanchao.mvvmdemo.utils.set
import com.ruanchao.mvvmdemo.view.LoadingDialog
import io.reactivex.*
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


class LoginViewModel(private val repo: LoginRepo): ViewModel() {

    var userName = MutableLiveData<String>().apply {
        value = null
    }
    var pwd = MutableLiveData<String>().apply {
        value = null
    }
    var rePwd = MutableLiveData<String>().apply {
        value = null
    }
    var userInfoData = MutableLiveData<User>().apply {
        value = null
    }

    var registerError = MutableLiveData<String>().apply{
            value = null
        }
    var loginError = MutableLiveData<String>().apply {
        value = null
    }
    var isLoading = MutableLiveData<Boolean>().apply {
        value = false
    }

    val TAG = LoginViewModel::class.java.simpleName


    fun login(){
        if (TextUtils.isEmpty(userName.value) || TextUtils.isEmpty(pwd.value)){
            loginError.set("用户名或者密码不能为空")
            return
        }
        repo.login(userName.value!!, pwd.value!!)
            .schedule()
            .subscribe(object : Observer<BaseNetBean<User>>{
                override fun onComplete() {
                    isLoading.set(false)

                }

                override fun onSubscribe(d: Disposable) {
                    isLoading.set(true)
                }

                override fun onNext(t: BaseNetBean<User>) {
                    if (t.errorCode == 0){
                        userInfoData.set(t.data)
                    }else{
                        loginError.set(t.errorMsg)
                    }
                }

                override fun onError(e: Throwable) {
                    loginError.set(e.message)
                }

            })
    }

    fun register(){
        Log.i(TAG, "userName:${userName.value} pwd:${pwd.value} repwd:${rePwd.value}")
        if (TextUtils.isEmpty(userName.value)
            || TextUtils.isEmpty(pwd.value)){
            registerError.set("用户名或者密码不能为空")
            return
        }else if (pwd.value != rePwd.value){
            registerError.set("两次密码不一致")
        }
        repo.register(userName.value!!, pwd.value!!, rePwd.value!!)
            .schedule()
            .subscribe(object : Observer<BaseNetBean<User>>{
                override fun onComplete() {
                    isLoading.set(false)
                }

                override fun onSubscribe(d: Disposable) {
                    isLoading.set(true)
                }

                override fun onNext(t: BaseNetBean<User>) {
                    if (t.errorCode == 0){
                        userInfoData.set(t.data)
                    }else{
                        registerError.set(t.errorMsg)
                    }
                }

                override fun onError(e: Throwable) {
                    registerError.set(e.message)
                }

            })
    }

}