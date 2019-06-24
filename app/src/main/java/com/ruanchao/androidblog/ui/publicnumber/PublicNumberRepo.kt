package com.ruanchao.androidblog.ui.publicnumber

import com.ruanchao.androidblog.bean.BaseNetBean
import com.ruanchao.androidblog.bean.PublicNumberInfo
import com.ruanchao.androidblog.bean.PublicNumerArticalInfo
import com.ruanchao.androidblog.net.WanAndroidApi
import com.ruanchao.androidblog.ui.collection.CollectionRepo
import io.reactivex.Observable

class PublicNumberRepo(val remote: WanAndroidApi): CollectionRepo(remote){

    fun getPublicNumberList(): Observable<BaseNetBean<List<PublicNumberInfo>>>
    = remote.getPublicNumberList()

    fun getPublicNumberDataList(id: Int, page:Int): Observable<BaseNetBean<PublicNumerArticalInfo>>
    = remote.getPublicNumberDataList(id,page)

}