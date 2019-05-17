package com.ruanchao.mvpframe.Home

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.ruanchao.mvpframe.R
import kotlinx.android.synthetic.main.activity_blog_detail.*

class BlogDetailActivity : AppCompatActivity() {

    companion object {
        val BLOG_DETAIL_URL: String = "BLOG_DETAIL_URL"
        fun start(context: Context, url: String){
            var intent: Intent = Intent(context, BlogDetailActivity::class.java)
            intent.putExtra(BLOG_DETAIL_URL, url)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog_detail)
        initView()
    }

    private fun initView() {
        AgentWeb.with(this)
            .setAgentWebParent(ll_blog_web,LinearLayout.LayoutParams(-1,-1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(intent.getStringExtra(BLOG_DETAIL_URL))
    }
}
