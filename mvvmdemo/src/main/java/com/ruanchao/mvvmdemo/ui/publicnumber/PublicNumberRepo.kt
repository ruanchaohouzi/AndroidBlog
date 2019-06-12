package com.ruanchao.mvvmdemo.ui.publicnumber

import com.ruanchao.mvvmdemo.bean.BaseNetBean
import com.ruanchao.mvvmdemo.bean.PublicNumberInfo
import com.ruanchao.mvvmdemo.bean.PublicNumerArticalInfo
import com.ruanchao.mvvmdemo.net.WanAndroidApi
import io.reactivex.Observable

class PublicNumberRepo(val remote: WanAndroidApi){

    fun getPublicNumberList(): Observable<BaseNetBean<List<PublicNumberInfo>>>
    = remote.getPublicNumberList()

    fun getPublicNumberDataList(id: Int, page:Int): Observable<BaseNetBean<PublicNumerArticalInfo>>
    = remote.getPublicNumberDataList(id,page)

}