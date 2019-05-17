package com.ruanchao.mvpframe

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.ruanchao.mvpframe.basemvp.basepresenter.BasePresenter
import com.ruanchao.mvpframe.basemvp.baseview.IBaseView

abstract class BaseMvpFragment<V: IBaseView, P: BasePresenter<V>>: Fragment() {

    var mPresenter: P? = null;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = createPresenter()
        mPresenter?.attachView(this as V)
    }

    protected abstract fun createPresenter(): P?

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }

}