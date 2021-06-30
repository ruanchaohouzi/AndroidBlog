package com.ruanchao.androidblog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ruanchao.androidblog.databinding.ActivityMainBinding
import com.ruanchao.androidblog.ui.collection.CollectionFragment
import com.ruanchao.androidblog.ui.home.HomeBlogFragment
import com.ruanchao.androidblog.ui.knowledge.KnowledgeFragment
import com.ruanchao.androidblog.ui.login.LoginViewModel
import com.ruanchao.androidblog.ui.user.UserFragment
import com.ruanchao.androidblog.ui.publicnumber.PublicNumberFragment
import com.ruanchao.androidblog.utils.PreferencesUtil
import com.ruanchao.androidblog.utils.obtainViewModel
import com.ruanchao.androidblog.utils.toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var clickStartTime:Long = 0
    private val CLICK_INTERVAL: Long = 2000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        supportActionBar?.hide()
        //第一次进入，判断用户是否已经登录
//        val cookie = PreferencesUtil.getSet(PreferencesUtil.COOKIE_KEY)
//        if (cookie != null){
//            val loginViewModel = obtainViewModel(LoginViewModel::class.java)
//            loginViewModel.getUserInfo()
//        }

        initTab()
    }

    private fun initTab() {
        val fragments = ArrayList<Fragment>()
        fragments.add(PublicNumberFragment())
        fragments.add(HomeBlogFragment())
        fragments.add(KnowledgeFragment())
        fragments.add(CollectionFragment())
        fragments.add(UserFragment())
        home_tab_layout.initView(supportFragmentManager, fragments)
    }

    override fun onBackPressed() {
        exit()
    }

    private fun exit(){

        if (System.currentTimeMillis() - clickStartTime > CLICK_INTERVAL){
            clickStartTime = System.currentTimeMillis()
            toast("再按一次退出应用")
        }else{
            super.onBackPressed()
        }
    }

}
