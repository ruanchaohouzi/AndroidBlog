package com.ruanchao.mvvmdemo.home

import com.ruanchao.mvvmdemo.bean.BannerInfo
import com.ruanchao.mvvmdemo.bean.BaseNetBean
import com.ruanchao.mvvmdemo.bean.Projects
import com.ruanchao.mvvmdemo.net.RequestApi
import com.ruanchao.mvvmdemo.bean.BlogContent
import com.ruanchao.mvvmdemo.db.BlogContentDao
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe

class HomeBlogRepo(private val remote: RequestApi, private val local: BlogContentDao){


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