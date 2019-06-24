package com.ruanchao.androidblog.view

import com.ruanchao.androidblog.ui.base.BaseFragment


abstract class LazyLoadFragment : BaseFragment() {
    private var isViewCreated: Boolean = false // 界面是否已创建完成
    private var isVisibleToUser: Boolean = false // 是否对用户可见
    protected var isCanLoadData: Boolean = false //可以加载数据

    // 实现具体的数据请求逻辑
    protected abstract fun loadData()

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        tryLoadData()
    }

    override fun initData(){
        isViewCreated = true;
        tryLoadData();
    }

    fun tryLoadData() {
        if (isViewCreated && isVisibleToUser) {
            isCanLoadData = true
            loadData()
        }
    }
}
