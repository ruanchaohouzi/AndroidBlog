package com.ruanchao.androidblog.ui.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ruanchao.androidblog.bean.UserInfo
import com.ruanchao.androidblog.databinding.UserFragmentLayoutBinding
import com.ruanchao.androidblog.event.LoginUserMsg
import com.ruanchao.androidblog.ui.base.BaseFragment
import com.ruanchao.androidblog.ui.login.LoginActivity
import com.ruanchao.androidblog.ui.login.LoginViewModel
import com.ruanchao.androidblog.utils.obtainViewModel
import com.ruanchao.androidblog.utils.toast
import kotlinx.android.synthetic.main.user_fragment_layout.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class UserFragment: BaseFragment() {

    lateinit var viewBinding: UserFragmentLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        viewBinding = UserFragmentLayoutBinding.inflate(inflater, container, false)
            .apply {
                viewModel = (activity as AppCompatActivity).obtainViewModel(LoginViewModel::class.java)
                lifecycleOwner = this@UserFragment
            }
        return viewBinding.root

    }

    override fun reload() {
    }

    override fun initData() {

        UserInfo.getInstance()?.user?.let {
            tv_userName.text = it.username
            btn_login.text = "退出账号"
        }

        btn_login.setOnClickListener {
            if (UserInfo.getInstance()?.user == null){
                LoginActivity.start(activity as Context)
            }else{
                viewBinding.viewModel?.logout()
            }
        }

        viewBinding.viewModel?.isLoading?.observe(this, Observer {
            if (it){
                showLoadingDialog()
            }else{
                closeLoadingDialog()
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(userMsg: LoginUserMsg){
        if (userMsg.user == null){
            toast("退出成功")
            tv_userName.text = "请登录"
            btn_login.text = "登录"
        }else {
            tv_userName.text = userMsg.user?.username
            btn_login.text = "退出登录"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}