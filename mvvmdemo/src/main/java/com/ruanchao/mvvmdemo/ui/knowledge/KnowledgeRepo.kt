package com.ruanchao.mvvmdemo.ui.knowledge

import com.ruanchao.mvvmdemo.bean.BaseNetBean
import com.ruanchao.mvvmdemo.net.WanAndroidApi
import io.reactivex.Observable

class KnowledgeRepo(val remote: WanAndroidApi){

    fun getKnowledgeList() = remote.getKnowledgeList()

    fun getKnowledgeChildList(page: Int, cid:Int) = remote.getKnowledgeChildList(page, cid)



}