package com.ruanchao.mvvmdemo.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.ruanchao.mvpframe.utils.StatusBarUtil
import com.ruanchao.mvvmdemo.R
import kotlinx.android.synthetic.main.activity_blog_detail.*
import kotlinx.android.synthetic.main.home_fragment_layout.*
import kotlinx.android.synthetic.main.public_number_fragment_layout.*

class BlogDetailActivity : AppCompatActivity() {

    companion object {
        const val BLOG_DETAIL_URL: String = "BLOG_DETAIL_URL"
        fun start(context: Context, url: String){
            val intent = Intent(context, BlogDetailActivity::class.java)
            intent.putExtra(BLOG_DETAIL_URL, url)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_detail)
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, ll_blog_web)
        initView()
    }

    private fun initView() {
        val url = intent.getStringExtra(BLOG_DETAIL_URL)
        Log.i("BlogDetailActivity", url)
        AgentWeb.with(this)
            .setAgentWebParent(ll_blog_web,LinearLayout.LayoutParams(-1,-1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(url)
    }
}
