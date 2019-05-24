package com.ruanchao.mvvmdemo.model

import com.ruanchao.mvpframe.bean.BannerInfo
import com.ruanchao.mvpframe.bean.BaseNetBean
import com.ruanchao.mvpframe.bean.HomeData
import com.ruanchao.mvpframe.bean.Projects
import com.ruanchao.mvpframe.net.NetWorkManager
import com.ruanchao.mvpframe.net.RequestApi
import com.ruanchao.mvvmdemo.bean.BlogContent
import com.ruanchao.mvvmdemo.db.BlogContentDao
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Single

class HomeBlogRepo(private val remote: RequestApi, private val local: BlogContentDao){


    fun getAllProjectByPageFormRemote(page: Int): Observable<BaseNetBean<Projects>> {
        return remote.getAllProjectByPage(page)
    }

    fun getBannerListFormRemote(): Observable<BaseNetBean<List<BannerInfo>>> {
        return remote.getBannerList()
    }

    fun getBlogContentFormLocal(): Observable<MutableList<BlogContent>> {
        return Observable.create(object :ObservableOnSubscribe<MutableList<BlogContent>>{
             override fun subscribe(emitter: ObservableEmitter<MutableList<BlogContent>>) {
                 val blogContent = local.getBlogContent()
                 //必须要加这两项，不然concat下一个Observal无法执行，由于当前事件没有执行完成
                 emitter.onNext(blogContent)
                 emitter.onComplete()
             }
         })
    }

    fun addOrUpdateBlogContent(blogContent: BlogContent) = local.addOrUpdateBlogContent(blogContent)

}