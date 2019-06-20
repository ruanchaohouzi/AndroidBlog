package com.ruanchao.mvvmdemo.ui.knowledge

import com.ruanchao.mvvmdemo.net.WanAndroidApi
import com.ruanchao.mvvmdemo.ui.collection.CollectionRepo

class KnowledgeRepo(val remote: WanAndroidApi): CollectionRepo(remote){

    fun getKnowledgeList() = remote.getKnowledgeList()

    fun getKnowledgeChildList(page: Int, cid:Int) = remote.getKnowledgeChildList(page, cid)



}