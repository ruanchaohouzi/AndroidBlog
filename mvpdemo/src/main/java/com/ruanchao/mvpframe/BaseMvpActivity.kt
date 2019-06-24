package com.ruanchao.mvpframe

import android.app.Activity
import android.os.Bundle
import com.ruanchao.mvpframe.basemvp.basepresenter.BasePresenter
import com.ruanchao.mvpframe.basemvp.baseview.IBaseView

abstract class BaseMvpActivity<V: IBaseView, P: BasePresenter<V>>: Activity() {

    var mPresenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter = createPresenter()
        mPresenter?.attachView(this as V)
    }

    protected abstract fun createPresenter():P

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }

}