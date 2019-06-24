package com.ruanchao.mvpframe.Home

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ruanchao.mvpframe.BaseMvpFragment
import com.ruanchao.mvpframe.R
import com.ruanchao.mvpframe.adapter.HomeAdapter
import com.ruanchao.mvpframe.bean.*
import com.ruanchao.mvpframe.utils.StatusBarUtil
import kotlinx.android.synthetic.main.home_fragment_layout.*

class HomeFragment: BaseMvpFragment<IHomeView,HomePresenter>(), IHomeView {

    private var homeDataList: MutableList<HomeData> = mutableListOf()

    var mHomeAdapter:HomeAdapter? = null

    var mCurrentPage: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(activity as Activity)
        StatusBarUtil.setPaddingSmart(activity as Activity, mHomeToolbar)

        mHomeAdapter = HomeAdapter(homeDataList,activity as Context)
        projectRecyclerView.run {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
            adapter = mHomeAdapter
        }
        var dividerItemDecoration: androidx.recyclerview.widget.DividerItemDecoration =
            androidx.recyclerview.widget.DividerItemDecoration(
                activity,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(activity as Context, R.drawable.home_recycler_item_divider)!!)
        projectRecyclerView.addItemDecoration(dividerItemDecoration)
        mHomeAdapter!!.setOnItemClickListener { position, projectInfo ->
                BlogDetailActivity.start(activity as Context, projectInfo.projectLink)
        }
        projectRecyclerView.addOnScrollListener(mRecyclerViewScrollListener)

        srf_home_refresh.setOnRefreshListener {
            loadData(0)
        }
        srf_home_refresh.isRefreshing = true
        mPresenter!!.getHomeData(mCurrentPage)
    }

    private fun loadData(page: Int) {
        mCurrentPage = page
        mPresenter!!.getAllProjectByPage(page)

    }

    override fun createPresenter(): HomePresenter? {
        return HomePresenter()
    }

    override fun onGetHomeDataFail(errMsg: String?) {
        Toast.makeText(activity,"数据获取失败", Toast.LENGTH_LONG).show()
        srf_home_refresh.isRefreshing = false
    }

    override fun onGetHomeDataSuccess(data: MutableList<HomeData>?, page: Int) {
        srf_home_refresh.isRefreshing = false

        if (data != null) {
            if (page == 0){
                mHomeAdapter?.setHomeDataLists(data)
            }else{
                mHomeAdapter?.addHomeDataList(data)
            }
            mHomeAdapter?.notifyDataSetChanged()
        }
    }

    override fun onGetProjectsSuccess(data: MutableList<HomeData>?, page: Int) {
        srf_home_refresh.isRefreshing = false
        if (data == null) {
            return
        }
        if (page == 0) {
            mHomeAdapter!!.refreshHomeDataLists(data)
        } else {
            mHomeAdapter!!.addHomeDataList(data)
        }
        mHomeAdapter?.notifyDataSetChanged()
    }

    override fun onGetProjectsFail(errMsg: String?) {}

    override fun onGetBannerListSuccess(data: BaseNetBean<List<BannerInfo>>?) {}

    override fun onGetBannerListFail(errMsg: String?) {}

    var mRecyclerViewScrollListener: androidx.recyclerview.widget.RecyclerView.OnScrollListener = object: androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            //当前RecyclerView显示出来的最后一个的item的position
            var lastPosition: Int = -1;
            //当前状态为停止滑动状态SCROLL_STATE_IDLE时
            if (newState == androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE) {
                var layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = recyclerView.layoutManager;
                lastPosition = (layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition();
                //时判断界面显示的最后item的position是否等于itemCount总数-1也就是最后一个item的position
                //如果相等则说明已经滑动到最后了
                if (lastPosition == recyclerView.getLayoutManager()!!.itemCount - 1) {
                    loadData(++mCurrentPage)
                }
            }
        }

        override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val linearLayoutManager = recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
            val firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
            if (firstPosition == 0) {
                mHomeToolbar.setBackgroundColor(getColor(activity as Context, R.color.color_translucent))
                mHomeSearchIcon.setImageResource(R.mipmap.home_ic_action_search_white)
                mHomeTitle.setText("")
            } else {
                if (mHomeAdapter!!.getHomeDataList().size > 1) {
                    mHomeToolbar.setBackgroundColor(getColor(activity as Context, R.color.color_home_title_bg))
                    mHomeSearchIcon.setImageResource(R.mipmap.home_ic_action_search_black)
                    mHomeTitle.setText("首页")
                }
            }
        }
    }

}