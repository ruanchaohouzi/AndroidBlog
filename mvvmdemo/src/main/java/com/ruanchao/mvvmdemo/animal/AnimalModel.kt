package com.ruanchao.mvvmdemo.animal

import com.ruanchao.mvpframe.bean.BaseNetBean
import com.ruanchao.mvpframe.bean.Projects
import com.ruanchao.mvpframe.net.NetWorkManager
import com.ruanchao.mvvmdemo.MainApplication
import com.ruanchao.mvvmdemo.bean.UserInfo
import com.ruanchao.mvvmdemo.db.UserDao
import com.ruanchao.mvvmdemo.db.UserDb
import com.ruanchao.mvvmdemo.utils.schedule
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.http.Path

class AnimalModel(val userDao: UserDao){


    fun getAllProjectByPage(page: Int): Observable<BaseNetBean<Projects>>{
        return NetWorkManager.getInstance().getRequestApi().getAllProjectByPage(page)
    }

    fun getUserInfoById(id: Int): Single<UserInfo> {
        return userDao.getUserInfoById(id).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    fun insertAll(list: List<UserInfo>): Observable<Boolean> {
        return Observable.create(ObservableOnSubscribe<Boolean> {
            userDao.insertAll(list)
        })
    }
}