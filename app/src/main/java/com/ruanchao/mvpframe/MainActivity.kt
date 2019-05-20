package com.ruanchao.mvpframe

import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.ruanchao.mvpframe.Home.HomeFragment
import com.ruanchao.mvpframe.utils.StatusBarUtil
import com.ruanchao.mvpframe.view.BottomTabLayoutView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    var mBottomTabLayoutView: BottomTabLayoutView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()



        mBottomTabLayoutView = findViewById<BottomTabLayoutView>(R.id.home_tab_layout)
        val fragments = ArrayList<Fragment>()
        fragments.add(HomeFragment())

        val page2 = Page1()
        val bundle2 = Bundle()
        bundle2.putString("page", "页面2222")
        page2.setArguments(bundle2)
        fragments.add(page2)

        val page3 = Page1()
        val bundle3 = Bundle()
        bundle3.putString("page", "页面3333")
        page3.setArguments(bundle3)
        fragments.add(page3)

        val page4 = Page1()
        val bundle4 = Bundle()
        bundle4.putString("page", "页面4444")
        page4.setArguments(bundle4)
        fragments.add(page4)

        val page5 = Page1()
        val bundle5 = Bundle()
        bundle5.putString("page", "页面55555")
        page5.setArguments(bundle5)
        fragments.add(page5)

        mBottomTabLayoutView!!.initView(supportFragmentManager, fragments)

    }
}
