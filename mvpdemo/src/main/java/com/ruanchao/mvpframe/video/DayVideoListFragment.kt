package com.ruanchao.mvpframe.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ruanchao.mvpframe.BaseMvpFragment

class DayVideoListFragment: BaseMvpFragment<IDayVideoView, DayVideoListPresenter>() {

    override fun createPresenter(): DayVideoListPresenter? {
        return DayVideoListPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}