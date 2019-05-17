package com.ruanchao.mvpframe.basemvp.basepresenter

import com.ruanchao.mvpframe.basemvp.baseview.IBaseView

interface IBasePresenter{
    /**
     * 关联Presenter和View
     */
    fun attachView(view: IBaseView)

    /**
     * 关联Presenter和View
     */
    fun isAttachView(): Boolean

    /**
     * 取消关联Presenter和View
     */
    fun detachView();

    /**
     * Rx订阅
     */
//    fun subscribe(observable: Observable<*>, subscriber: Subscriber<*>)

    /**
     * Rx取消订阅
     */
    fun unSubscribe()

}