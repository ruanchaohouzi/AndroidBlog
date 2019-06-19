package com.ruanchao.mvvmdemo.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.ruanchao.mvvmdemo.R

class LoginActivity : AppCompatActivity() {

    companion object {

        val REQUEST_CODE = 1000

        fun start(context: Context){
            context.startActivity(Intent(context, LoginActivity::class.java))
        }

        fun startActivityForResult(context: Activity, requestCode: Int){
            context.startActivityForResult(Intent(context, LoginActivity::class.java), requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    //app:defaultNavHost="true"这个属性意味着你的NavGraphFragment将会 拦截系统Back键的点击事件
    // （因为系统的back键会直接关闭Activity而非切换Fragment），
    // 你同时 必须重写 Activity的 onSupportNavigateUp() 方法

    override fun onSupportNavigateUp() = findNavController(this, R.id.login_nav_graph).navigateUp()
}
