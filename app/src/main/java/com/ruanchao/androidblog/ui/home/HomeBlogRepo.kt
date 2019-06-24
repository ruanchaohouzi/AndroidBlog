package com.ruanchao.androidblog.ui.home

import com.ruanchao.androidblog.bean.BannerInfo
import com.ruanchao.androidblog.bean.BaseNetBean
import com.ruanchao.androidblog.bean.Projects
import com.ruanchao.androidblog.net.WanAndroidApi
import com.ruanchao.androidblog.bean.BlogContent
import com.ruanchao.androidblog.db.BlogContentDao
import com.ruanchao.androidblog.ui.collection.CollectionRepo
import io.reactivex.Observable

class HomeBlogRepo(private val remote: WanAndroidApi, private val local: BlogContentDao): CollectionRepo(remote){


    fun getAllProjectByPageFormRemote(page: Int): Observable<BaseNetBean<Projects>> {
        return remote.getAllProjectByPage(page)
    }

    fun getBannerListFormRemote(): Observable<BaseNetBean<List<BannerInfo>>> {
        return remote.getBannerList()
    }

    fun getBlogContentFormLocal(): Observable<MutableList<BlogContent>> {
        return Observable.create(){
            val blogContent = local.getBlogContent()
            //必须要加这两项，不然concat下一个Observal无法执行，由于当前事件没有执行完成
            it.onNext(blogContent)
            it.onComplete()
        }
    }

    fun addOrUpdateBlogContent(blogContent: BlogContent) = local.addOrUpdateBlogContent(blogContent)

}