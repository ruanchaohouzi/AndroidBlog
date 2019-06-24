package com.ruanchao.androidblog.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.text.TextUtils
import android.util.Log
import com.ruanchao.androidblog.bean.BaseNetBean
import com.ruanchao.androidblog.bean.User
import com.ruanchao.androidblog.bean.UserInfo
import com.ruanchao.androidblog.event.LoginUserMsg
import com.ruanchao.androidblog.utils.PreferencesUtil
import com.ruanchao.androidblog.utils.schedule
import com.ruanchao.androidblog.utils.set
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus


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
            .flatMap {
                if (it.errorCode == 0 && it.data != null){
                    repo.insert(it.data!!)
                }
                Observable.just(it)
            }
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
                        handleLoginSuccess(t.data)
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
            .flatMap {
                if (it.errorCode == 0 && it.data != null){
                    repo.insert(it.data!!)
                }
                Observable.just(it)
            }
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
                        handleLoginSuccess(t.data)
                    }else{
                        registerError.set(t.errorMsg)
                    }
                }

                override fun onError(e: Throwable) {
                    registerError.set(e.message)
                }

            })
    }

    fun logout(){
        repo.logout()
            .schedule()
            .subscribe(object : Observer<BaseNetBean<Any>>{
                override fun onComplete() {
                    isLoading.set(false)
                }

                override fun onSubscribe(d: Disposable) {
                    isLoading.set(true)
                }

                override fun onNext(t: BaseNetBean<Any>) {
                    if (t.errorCode == 0){
                        EventBus.getDefault().post(LoginUserMsg(null))
                        UserInfo.getInstance()?.user = null
                        PreferencesUtil.remove(PreferencesUtil.COOKIE_KEY)
                    }else{
                        loginError.value = t.errorMsg
                    }
                }

                override fun onError(e: Throwable) {
                    loginError.value = e.message
                }

            })
    }

    fun getUserInfo(){
        repo.getUserInfo()?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : SingleObserver<User>{
                override fun onSuccess(t: User) {
                    UserInfo.getInstance()?.user = t
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                }

            })

    }

    private fun handleLoginSuccess(user: User?){
        userInfoData.set(user)
        UserInfo.getInstance()?.user = user

        //需要发送粘性广播???
        EventBus.getDefault().post(LoginUserMsg(user))
    }

}