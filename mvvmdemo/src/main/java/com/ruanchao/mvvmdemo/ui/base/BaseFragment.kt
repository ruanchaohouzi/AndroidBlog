package com.ruanchao.mvvmdemo.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.view.LoadingDialog
import com.ruanchao.mvvmdemo.view.MultiStateView
import kotlinx.android.synthetic.main.public_number_fragment_layout.*

abstract class BaseFragment: Fragment() {

    private lateinit var rootView: View
    var loadingDialog: LoadingDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = initView(inflater, container, savedInstanceState)
        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //经过测试发现，在onCreateView中拿到View必须通过root.findViewById，而不能直接使用控件的id直接拿到对象
        //而在onActivityCreated可以通过控件的id直接拿到对象
        var stateViewRoot: MultiStateView? = rootView.findViewById(R.id.stateView)
        val errView = stateViewRoot?.getView(MultiStateView.VIEW_STATE_ERROR)
        errView?.findViewById<Button>(R.id.btn_reload)?.setOnClickListener{
            stateView.viewState = MultiStateView.VIEW_STATE_LOADING
            reload()
        }
        initData()
    }

    fun showLoadingDialog(){
        loadingDialog = LoadingDialog()
        loadingDialog?.show(fragmentManager!!,"loading")

    }

    fun closeLoadingDialog(){
        loadingDialog?.dismiss()
    }

    abstract fun reload()

    abstract fun initData()

    abstract fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View

}