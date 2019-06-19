package com.ruanchao.mvvmdemo.ui.login

import androidx.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.databinding.LoginFragmentLayoutBinding
import com.ruanchao.mvvmdemo.ui.base.BaseFragment
import com.ruanchao.mvvmdemo.utils.obtainViewModel
import com.ruanchao.mvvmdemo.utils.toast
import kotlinx.android.synthetic.main.login_fragment_layout.*

class LoginFragment: BaseFragment() {

    lateinit var viewBinding: LoginFragmentLayoutBinding

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        viewBinding = LoginFragmentLayoutBinding.inflate(inflater, container, false)
            .apply {
                viewModel = (activity as AppCompatActivity).obtainViewModel(LoginViewModel::class.java)
                lifecycleOwner = this@LoginFragment
            }
        return viewBinding.root
    }

    override fun reload() {
    }

    override fun initData() {
        tv_register.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_register)
        }
        viewBinding.viewModel?.loginError?.observe(this, Observer {
            it?.let {
                activity!!.toast(it)
            }

        })

        viewBinding.viewModel?.userInfoData?.observe(this, Observer {
            it?.let {
                activity!!.toast("登录成功")
                activity!!.finish()
            }

        })

        viewBinding.viewModel?.isLoading?.observe(this, Observer {
            if (it){
                showLoadingDialog()
            }else{
                closeLoadingDialog()
            }
        })
    }

}