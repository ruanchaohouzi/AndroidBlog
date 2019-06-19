package com.ruanchao.mvvmdemo.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ruanchao.mvvmdemo.bean.UserInfo
import com.ruanchao.mvvmdemo.databinding.UserFragmentLayoutBinding
import com.ruanchao.mvvmdemo.event.LoginUserMsg
import com.ruanchao.mvvmdemo.ui.base.BaseFragment
import com.ruanchao.mvvmdemo.utils.obtainViewModel
import com.ruanchao.mvvmdemo.utils.set
import com.ruanchao.mvvmdemo.utils.toast
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

        viewBinding.viewModel?.isLogout?.observe(this, Observer {
            it?.let { logout ->
                //清空，避免多次重复回调弹出toast
                viewBinding.viewModel?.isLogout!!.set(null)
                if (logout){
                    toast("退出成功")
                    tv_userName.text = "请登录"
                    btn_login.text = "登录"
                }else{
                    toast("退出失败")
                }
            }

        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(userMsg: LoginUserMsg){
        tv_userName.text = userMsg.user?.username
        btn_login.text = "退出登录"
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}