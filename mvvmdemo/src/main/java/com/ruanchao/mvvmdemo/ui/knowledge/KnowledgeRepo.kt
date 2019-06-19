package com.ruanchao.mvvmdemo.ui.knowledge

import com.ruanchao.mvvmdemo.net.WanAndroidApi
import com.ruanchao.mvvmdemo.ui.login.BaseRepo

class KnowledgeRepo(val remote: WanAndroidApi): BaseRepo(remote){

    fun getKnowledgeList() = remote.getKnowledgeList()

    fun getKnowledgeChildList(page: Int, cid:Int) = remote.getKnowledgeChildList(page, cid)



}