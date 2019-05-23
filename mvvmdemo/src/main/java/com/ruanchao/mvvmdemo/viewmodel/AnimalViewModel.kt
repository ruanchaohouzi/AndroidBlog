package com.ruanchao.mvvmdemo.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.BindingAdapter
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ruanchao.mvpframe.bean.BaseNetBean
import com.ruanchao.mvpframe.bean.Projects
import com.ruanchao.mvvmdemo.MainApplication
import com.ruanchao.mvvmdemo.bean.Animal
import com.ruanchao.mvvmdemo.bean.UserInfo
import com.ruanchao.mvvmdemo.db.UserDao
import com.ruanchao.mvvmdemo.db.UserDb
import com.ruanchao.mvvmdemo.model.AnimalModel
import com.ruanchao.mvvmdemo.utils.schedule
import com.ruanchao.mvvmdemo.utils.set
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

//必须要继承于ViewModel()
class AnimalViewModel(val animal: Animal): ViewModel(){

    val mCompositeDisposable : CompositeDisposable by lazy {
        CompositeDisposable()
    }

    val mModel: AnimalModel by lazy {
        var userDao = UserDb.getInstance(MainApplication.context!!)!!.userDao()
        AnimalModel(userDao)
    }

    //需要绑定的数据  采取了LiveData
    val shutCount = MutableLiveData<String>().set("${animal.name}一共叫了${animal.shoutCount}声")
    val projectCount = MutableLiveData<String>().set("projectCount:0")
    val shouldLoading = MutableLiveData<Boolean>().set(false)
    val error = MutableLiveData<Throwable>()
    val imageUrl = MutableLiveData<String>().set("https://static.fotor.com.cn/assets/projects/pages/59b42190-65a4-11e9-a7c3-fb5a346791c8_ce69c6d8-f32e-430f-a367-52d16d127c7e_thumb.jpg")


    //binding
    fun shout(){
        animal.shoutCount++
        //更新数据后，重新设置绑定的数据
        shutCount.set("${animal.name}一共叫了${animal.shoutCount}声")
    }

    fun getAllProjectByPage(){
        val subscribe = mModel.getAllProjectByPage(0)
            .schedule()
            .delay(5000, TimeUnit.MILLISECONDS) //为了加载效果更明显，这里延迟1秒
                //引入加载等待框
            .doOnSubscribe { shouldLoading.set(true) } //开始请求数据，设置加载为true
            .doOnNext { shouldLoading.set(false) }////请求完成，设置加载为false
            .doOnError { shouldLoading.set(false) }//请求出错，设置加载为false
            .subscribe({ it: BaseNetBean<Projects>? ->
                projectCount.set("projectCount:${it?.data?.datas?.size ?: 0}")

            }, { it: Throwable ->
                projectCount.set("projectCount:0")
                error.set(it)

            })
        mCompositeDisposable.add(subscribe)
    }

    fun destroy(){
        mCompositeDisposable.clear()
    }

    fun insert(){
        var userInfo1 : UserInfo = UserInfo()
        userInfo1.userId = 1
        userInfo1.userName = "rc"
        userInfo1.pwd = "12133"

        var userInfo2 : UserInfo = UserInfo()
        userInfo2.userId = 1
        userInfo2.userName = "rc"
        userInfo2.pwd = "12133"
        var list:ArrayList<UserInfo> = ArrayList()
        list.add(userInfo1)
        list.add(userInfo2)
        mModel.insertAll(list)
            .schedule()
            .subscribe {

            }
    }

    fun getUser(){
        mModel.getUserInfoById(1)
            .subscribe(
                {it:UserInfo ->
                    Log.i("test", it.userName)
                },
                {
                    Log.i("test", it.message)
                }
            )
    }


}