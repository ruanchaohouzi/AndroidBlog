package com.ruanchao.mvvmdemo.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.databinding.UserFragmentLayoutBinding
import com.ruanchao.mvvmdemo.event.UserMsg
import com.ruanchao.mvvmdemo.ui.base.BaseFragment
import com.ruanchao.mvvmdemo.utils.obtainViewModel
import kotlinx.android.synthetic.main.user_fragment_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class UserFragment: BaseFragment() {

    lateinit var viewBinding: UserFragmentLayoutBinding

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        viewBinding = UserFragmentLayoutBinding.inflate(inflater, container, false)
            .apply {
                viewModel = (activity as AppCompatActivity).obtainViewModel(LoginViewModel::class.java)
                lifecycleOwner = this@UserFragment
            }
        EventBus.getDefault().register(this)

        return viewBinding.root

    }

    override fun reload() {
    }

    override fun initData() {

        btn_login.setOnClickListener {

            LoginActivity.start(activity as Context)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(userMsg: UserMsg){
        tv_userName.text = userMsg.user.username
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}