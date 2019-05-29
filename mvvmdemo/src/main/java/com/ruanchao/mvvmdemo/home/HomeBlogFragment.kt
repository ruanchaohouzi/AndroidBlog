package com.ruanchao.mvvmdemo.home

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ruanchao.mvvmdemo.adapter.HomeBlogAdapter
import com.ruanchao.mvvmdemo.bean.HomeData
import com.ruanchao.mvpframe.utils.StatusBarUtil
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.databinding.HomeBlogFragmentBinding
import com.ruanchao.mvvmdemo.utils.obtainViewModel
import kotlinx.android.synthetic.main.home_blog_fragment.view.*
import kotlinx.android.synthetic.main.home_fragment_layout.*

class HomeBlogFragment: Fragment() {

    private lateinit var dataBindingView: HomeBlogFragmentBinding

    private var homeDataList: MutableList<HomeData> = mutableListOf()

    private var mBlogHomeAdapter: HomeBlogAdapter? = null

    var mCurrentPage: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val viewModelValue = obtainViewModel(this, HomeBlogViewModel::class.java)
        dataBindingView = HomeBlogFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = viewModelValue
            //必须要绑定生命周期，不写没效果,这样就可以增加监听器 监听数据的变化
            lifecycleOwner = this@HomeBlogFragment
        }
        return dataBindingView.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initStatuBar()
        setupRecyclerAdapter()
        initObserver()

        dataBindingView.root.srf_home_refresh.setOnRefreshListener {
            loadData(0)
        }
        srf_home_refresh.isRefreshing = true
        dataBindingView.viewModel?.getHomeData(mCurrentPage)

    }

    private fun initStatuBar() {
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(activity as Activity)
        StatusBarUtil.setPaddingSmart(activity as Activity, mHomeToolbar)
    }

    private fun initObserver() {
        //用于观察ViewModel中的数据变化,也可以采取BindingAdapter进行数据的通知操作
        //已采取BindingAdapter方式实现
//        dataBindingView.viewModel?.homeDataList?.observe(dataBindingView.lifecycleOwner!!, Observer {
//            if (it != null && it.size > 0) {
//                mBlogHomeAdapter?.setHomeDataLists(it)
//                srf_home_refresh.isRefreshing = false
//            }
//        })

        //用于观察ViewModel中的刷新数据变化（上拉，下拉）
        dataBindingView.viewModel?.refreshHomeDataList?.observe(dataBindingView.lifecycleOwner!!, Observer {
            if (it != null && it.size > 0) {
                if (mCurrentPage == 0){
                    mBlogHomeAdapter?.refreshHomeDataLists(it)
                }else{
                    mBlogHomeAdapter?.addHomeDataList(it)
                }
//                srf_home_refresh.isRefreshing = false
            }
        })

        //用于观察ViewModel中的错误信息
        dataBindingView.viewModel?.errorInfo?.observe(dataBindingView.lifecycleOwner!!, Observer {
            if (!it.isNullOrEmpty()){
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
//                srf_home_refresh.isRefreshing = false
            }
        })
    }

    private fun loadData(page: Int) {
        mCurrentPage = page
        dataBindingView.viewModel?.getAllProjectByPage(page)

    }

    private fun setupRecyclerAdapter() {
        mBlogHomeAdapter = HomeBlogAdapter(homeDataList,activity as Context, dataBindingView.viewModel)

        val dividerItemDecoration = DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(activity as Context, R.drawable.home_recycler_item_divider)!!)
        projectRecyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = mBlogHomeAdapter
            this.addItemDecoration(dividerItemDecoration)
        }
        mBlogHomeAdapter?.setOnItemClickListener {
            p ->
            BlogDetailActivity.start(activity!!, p.projectLink)
        }
        projectRecyclerView.addOnScrollListener(mRecyclerViewScrollListener)
    }

    private var mRecyclerViewScrollListener: RecyclerView.OnScrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            //当前RecyclerView显示出来的最后一个的item的position
            var lastPosition: Int = -1
            //当前状态为停止滑动状态SCROLL_STATE_IDLE时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val layoutManager: RecyclerView.LayoutManager? = recyclerView.layoutManager
                lastPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
                //如果相等则说明已经滑动到最后了
                if (lastPosition == recyclerView.layoutManager!!.itemCount - 1) {
                    loadData(++mCurrentPage)
                }
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstPosition = linearLayoutManager.findFirstVisibleItemPosition()
            if (firstPosition == 0) {
                mHomeToolbar.setBackgroundColor(ContextCompat.getColor(activity as Context, R.color.color_translucent))
                mHomeSearchIcon.setImageResource(R.mipmap.home_ic_action_search_white)
                mHomeTitle.text = ""
            } else {
                if (mBlogHomeAdapter!!.getHomeDataList().size > 1) {
                    mHomeToolbar.setBackgroundColor(
                        ContextCompat.getColor(
                            activity as Context,
                            R.color.color_home_title_bg
                        )
                    )
                    mHomeSearchIcon.setImageResource(R.mipmap.home_ic_action_search_black)
                    mHomeTitle.text = "首页"
                }
            }
        }
    }

}