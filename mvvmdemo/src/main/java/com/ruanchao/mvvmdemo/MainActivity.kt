package com.ruanchao.mvvmdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ruanchao.mvvmdemo.home.HomeBlogFragment
import com.ruanchao.mvvmdemo.ui.login.LoginFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fl_layout, LoginFragment())
            .commit()
    }
}
