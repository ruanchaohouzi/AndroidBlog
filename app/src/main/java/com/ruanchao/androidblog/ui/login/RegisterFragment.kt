package com.ruanchao.androidblog.ui.login

import androidx.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.ruanchao.androidblog.R
import com.ruanchao.androidblog.databinding.RegisterFragmentLayoutBinding
import com.ruanchao.androidblog.ui.base.BaseFragment
import com.ruanchao.androidblog.utils.obtainViewModel
import com.ruanchao.androidblog.utils.toast
import kotlinx.android.synthetic.main.register_fragment_layout.*

class RegisterFragment: BaseFragment() {

    lateinit var viewBinding: RegisterFragmentLayoutBinding

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = RegisterFragmentLayoutBinding.inflate(inflater, container, false)
            .apply {
                viewModel = (activity as AppCompatActivity).obtainViewModel(LoginViewModel::class.java)
                lifecycleOwner = this@RegisterFragment
            }
        return viewBinding.root
    }

    override fun initData() {
        tv_Login.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_login)
        }
        viewBinding.viewModel?.registerError?.observe(this, Observer {
            it?.let {
                activity!!.toast(it)
            }

        })

        viewBinding.viewModel?.userInfoData?.observe(this, Observer {
            it?.let {
                activity!!.toast("注册成功")
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

    override fun reload() {
    }

}