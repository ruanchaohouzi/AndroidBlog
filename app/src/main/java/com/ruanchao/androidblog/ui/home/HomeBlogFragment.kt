package com.ruanchao.androidblog.ui.home

import android.app.Activity
import androidx.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ruanchao.androidblog.adapter.HomeBlogAdapter
import com.ruanchao.androidblog.bean.HomeData
import com.ruanchao.mvpframe.utils.StatusBarUtil
import com.ruanchao.androidblog.R
import com.ruanchao.androidblog.bean.ErrorInfo
import com.ruanchao.androidblog.databinding.HomeBlogFragmentBinding
import com.ruanchao.androidblog.ui.base.BaseFragment
import com.ruanchao.androidblog.utils.obtainViewModel
import com.ruanchao.androidblog.view.MultiStateView.VIEW_STATE_CONTENT
import com.ruanchao.androidblog.view.MultiStateView.VIEW_STATE_ERROR
import kotlinx.android.synthetic.main.home_blog_fragment.*
import kotlinx.android.synthetic.main.home_blog_fragment.view.*


class HomeBlogFragment: BaseFragment() {

    private lateinit var dataBindingView: HomeBlogFragmentBinding

    private var homeDataList: MutableList<HomeData> = mutableListOf()

    private var mBlogHomeAdapter: HomeBlogAdapter? = null

    var mCurrentPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun reload() {
        dataBindingView.viewModel?.getHomeData(mCurrentPage)
    }

    override fun initData() {
        initStatuBar()
        setupRecyclerAdapter()
        initObserver()

        dataBindingView.root.srf_home_refresh.setOnRefreshListener {
            loadData(0)
        }
        srf_home_refresh.isRefreshing = true
        dataBindingView.viewModel?.getHomeData(mCurrentPage)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dataBindingView = HomeBlogFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = obtainViewModel(this@HomeBlogFragment,HomeBlogViewModel::class.java)
            //必须要绑定生命周期，不写没效果,这样就可以增加监听器 监听数据的变化
            lifecycleOwner = this@HomeBlogFragment
        }
        return dataBindingView.root
    }

    private fun initStatuBar() {
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(activity as Activity)
        StatusBarUtil.setPaddingSmart(activity as Activity, mHomeToolbar)
    }

    /**
     * 必须要设置 lifecycleOwner  这样观察者才会生效
     */
    private fun initObserver() {
        //用于观察ViewModel中的数据变化,也可以采取BindingAdapter进行数据的通知操作
        //已采取BindingAdapter方式实现
        dataBindingView.viewModel?.homeDataList?.observe(dataBindingView.lifecycleOwner!!, Observer {
            Log.i("HomeBlogViewModel", "homeDataList:${it.size}")
            if (it != null && it.size > 0) {
                mBlogHomeAdapter?.setHomeDataLists(it)
                stateView.viewState = VIEW_STATE_CONTENT
            }
        })

        //用于观察ViewModel中的刷新数据变化（上拉，下拉）
        dataBindingView.viewModel?.refreshHomeDataList?.observe(this, Observer {
            if (it != null && it.size > 0) {
                if (mCurrentPage == 0){
                    mBlogHomeAdapter?.refreshHomeDataLists(it)
                }else{
                    mBlogHomeAdapter?.addHomeDataList(it)
                }
            }
        })

        //用于观察ViewModel中的错误信息
        dataBindingView.viewModel?.errorInfo?.observe(dataBindingView.lifecycleOwner!!, Observer {
            if (it!=null){
                Toast.makeText(activity, it.errMsg, Toast.LENGTH_LONG).show()
                if (it.errType == ErrorInfo.ERROR_TYPE_LOAD
                    && mBlogHomeAdapter?.getHomeDataList()?.size ?: 0 == 0){
                    stateView.viewState = VIEW_STATE_ERROR
                }
            }
        })

        dataBindingView.viewModel?.collectErrInfo?.observe(this, Observer {
            it?.let {
                mBlogHomeAdapter?.notifyDataSetChanged()
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadData(page: Int) {
        mCurrentPage = page
        dataBindingView.viewModel?.getAllProjectByPage(page)

    }

    private fun setupRecyclerAdapter() {
        mBlogHomeAdapter = HomeBlogAdapter(homeDataList,activity as Context, dataBindingView.viewModel)

        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(
            activity,
            androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
        )
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(activity as Context, R.drawable.home_recycler_item_divider)!!)
        projectRecyclerView.run {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
            adapter = mBlogHomeAdapter
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
        mBlogHomeAdapter?.setOnItemClickListener {
            p ->
            BlogDetailActivity.start(activity!!, p.projectLink)
        }
        projectRecyclerView.addOnScrollListener(mRecyclerViewScrollListener)
    }

    private var mRecyclerViewScrollListener: androidx.recyclerview.widget.RecyclerView.OnScrollListener = object: androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            //当前RecyclerView显示出来的最后一个的item的position
            var lastPosition: Int = -1
            //当前状态为停止滑动状态SCROLL_STATE_IDLE时
            if (newState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE) {
                val layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = recyclerView.layoutManager
                lastPosition = (layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
                //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
                //如果相等则说明已经滑动到最后了
                if (lastPosition == recyclerView.layoutManager!!.itemCount - 1) {
                    loadData(++mCurrentPage)
                }
            }
        }

        override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val linearLayoutManager = recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
            val firstPosition = linearLayoutManager.findFirstVisibleItemPosition()
            if (firstPosition == 0) {
                mHomeToolbar.setBackgroundColor(ContextCompat.getColor(activity as Context, R.color.color_translucent))
                mHomeSearchIcon.visibility = View.GONE
                mHomeTitle.text = ""
            } else {
                if (mBlogHomeAdapter!!.getHomeDataList().size > 1) {
                    mHomeToolbar.setBackgroundColor(
                        ContextCompat.getColor(
                            activity as Context,
                            R.color.public_number_tab_bg
                        )
                    )
                    mHomeSearchIcon.visibility = View.VISIBLE
                    mHomeTitle.text = "首页"
                }
            }
        }

    }

}