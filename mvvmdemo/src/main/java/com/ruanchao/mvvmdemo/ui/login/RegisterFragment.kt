package com.ruanchao.mvvmdemo.ui.login

import androidx.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.bean.UserInfo1
import com.ruanchao.mvvmdemo.databinding.RegisterFragmentLayoutBinding
import com.ruanchao.mvvmdemo.event.UserMsg
import com.ruanchao.mvvmdemo.ui.base.BaseFragment
import com.ruanchao.mvvmdemo.utils.obtainViewModel
import com.ruanchao.mvvmdemo.utils.toast
import kotlinx.android.synthetic.main.register_fragment_layout.*
import org.greenrobot.eventbus.EventBus

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
                EventBus.getDefault().post(UserMsg(it))
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