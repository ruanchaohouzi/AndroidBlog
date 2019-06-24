package com.ruanchao.androidblog.ui.knowledge

import com.ruanchao.androidblog.net.WanAndroidApi
import com.ruanchao.androidblog.ui.collection.CollectionRepo

class KnowledgeRepo(val remote: WanAndroidApi): CollectionRepo(remote){

    fun getKnowledgeList() = remote.getKnowledgeList()

    fun getKnowledgeChildList(page: Int, cid:Int) = remote.getKnowledgeChildList(page, cid)



}