package com.ruanchao.mvpframe.basemvp.basepresenter

import com.ruanchao.mvpframe.basemvp.baseview.IBaseView
import rx.subscriptions.CompositeSubscription
import java.lang.ref.WeakReference

open abstract class BasePresenter<V : IBaseView> : IBasePresenter{

    protected var viewRef: WeakReference<V>? = null
    val mCompositeSubscription: CompositeSubscription by lazy {
        CompositeSubscription()
    }

    override fun attachView(view: IBaseView) {
        viewRef = WeakReference(view as V);
    }

    override fun isAttachView(): Boolean = (viewRef?.get() != null)

    override fun detachView() {
        viewRef?.clear()
        unSubscribe()
    }

//    override fun subscribe(observable: Observable<*>, subscriber: Subscriber<*>) {
//        val subscription = observable.observeOn(AndroidSchedulers.mainThread())
//            .subscribeOn(Schedulers.io())
//            .subscribe(subscriber)
//        mCompositeSubscription.add(subscription)
//    }

    override fun unSubscribe() {
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe()
        }
    }

}
