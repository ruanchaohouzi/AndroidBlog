package com.ruanchao.mvvmdemo.ui.login

import androidx.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ruanchao.mvvmdemo.bean.UserInfo1
import com.ruanchao.mvvmdemo.databinding.LoginFragmentLayoutBinding
import com.ruanchao.mvvmdemo.utils.obtainViewModel

class LoginFragment: androidx.fragment.app.Fragment() {

    private var mBindingView: LoginFragmentLayoutBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val viewModelValue = obtainViewModel(this, LoginViewModel::class.java)
        mBindingView = LoginFragmentLayoutBinding.inflate(inflater, container, false)
            .apply {
                viewModel = viewModelValue
            }
        return mBindingView!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initObserver()
    }

    private fun initObserver() {
        mBindingView?.viewModel?.userInfoData?.observe(this,
            Observer<UserInfo1> {
                Toast.makeText(activity as Context, "登录成功，用户为${it?.userId}",Toast.LENGTH_LONG).show()
//                (activity as MainActivity).supportFragmentManager.beginTransaction()
//                    .replace(R.id.fl_layout, HomeBlogFragment())
//                    .commit()
            })

        mBindingView?.viewModel?.error?.observe(this, Observer {
            Toast.makeText(activity as Context, "登录失败：$it",Toast.LENGTH_LONG).show()
        })
    }
}