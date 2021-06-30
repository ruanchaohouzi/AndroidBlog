package com.ruanchao.androidblog.ui.login2

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.ruanchao.androidblog.R
import com.ruanchao.androidblog.databinding.ActivityLogin2Binding
import com.ruanchao.androidblog.utils.toast
import kotlinx.android.synthetic.main.activity_login2.*

class LoginActivity : AppCompatActivity() {

    private lateinit var root: ActivityLogin2Binding

    private val viewModel: LoginViewModel2 by viewModels()

    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        root = DataBindingUtil.setContentView<ActivityLogin2Binding>(this, R.layout.activity_login2)
        init()
    }

    private fun init() {
        viewModel.userInfoData.observe(this, Observer{
            toast("登录成功")
        })

        viewModel.isLoading.observe(this, Observer{
            if (it){
                dialog = AlertDialog.Builder(this)
                    .setMessage("登录中")
                    .show()
            } else {
                dialog?.dismiss()
            }
        })

        viewModel.loginError.observe(this, Observer{
            toast(it)
        })

        root.btnSignIn.setOnClickListener {
            if (tv_user_name.text.toString().trim().isNullOrEmpty()
                || tv_password.text.toString().trim().isNullOrEmpty()){
                toast("请输入用户名或密码")
                return@setOnClickListener
            }
            viewModel.login(tv_user_name.text.toString(), tv_password.text.toString())
        }
    }


}
