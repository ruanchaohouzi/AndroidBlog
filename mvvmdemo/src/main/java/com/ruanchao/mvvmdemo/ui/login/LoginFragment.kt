package com.ruanchao.mvvmdemo.ui.login

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ruanchao.mvvmdemo.databinding.LoginFragmentLayoutBinding
import com.ruanchao.mvvmdemo.utils.obtainViewModel

class LoginFragment: Fragment() {

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



    }
}