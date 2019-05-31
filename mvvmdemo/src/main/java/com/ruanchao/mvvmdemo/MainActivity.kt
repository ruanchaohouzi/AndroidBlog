package com.ruanchao.mvvmdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.ruanchao.mvvmdemo.ui.home.HomeBlogFragment
import com.ruanchao.mvvmdemo.ui.publicnumber.PublicNumberFragment
import com.ruanchao.mvvmdemo.ui.tucao.TucaoFragment
import com.ruanchao.mvvmdemo.view.BottomTabLayoutView
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    var mBottomTabLayoutView: BottomTabLayoutView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        mBottomTabLayoutView = findViewById<BottomTabLayoutView>(R.id.home_tab_layout)
        val fragments = ArrayList<Fragment>()
        fragments.add(PublicNumberFragment())

        fragments.add(HomeBlogFragment())

        fragments.add(TucaoFragment())

        fragments.add(TucaoFragment())

        fragments.add(TucaoFragment())

        mBottomTabLayoutView!!.initView(supportFragmentManager, fragments)
    }
}
