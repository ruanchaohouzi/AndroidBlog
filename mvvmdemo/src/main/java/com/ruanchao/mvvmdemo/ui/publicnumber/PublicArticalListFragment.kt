package com.ruanchao.mvvmdemo.ui.publicnumber

import androidx.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.adapter.PublicNumberListAdapter
import com.ruanchao.mvvmdemo.databinding.PublicNumberArticalListFragmentLayoutBinding
import com.ruanchao.mvvmdemo.factory.ViewModelFactory
import com.ruanchao.mvvmdemo.ui.base.BaseFragment
import com.ruanchao.mvvmdemo.ui.home.BlogDetailActivity
import com.ruanchao.mvvmdemo.utils.obtainViewModel
import com.ruanchao.mvvmdemo.view.LazyLoadFragment
import com.ruanchao.mvvmdemo.view.MultiStateView
import kotlinx.android.synthetic.main.public_number_artical_list_fragment_layout.*


class PublicArticalListFragment: BaseFragment() {

    lateinit var viewModel: PublicNumberViewModel
    var mCurrentPage: Int = 1
    var mCurrentId: Int? = 408;
    private val _TAG = PublicArticalListFragment::class.java.simpleName

    companion object {
        fun newInstance(id: Int): PublicArticalListFragment {
            var f = PublicArticalListFragment()
            var bundle = Bundle()
            bundle.putInt("id", id)
            f.arguments = bundle
            return f
        }
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = obtainViewModel(this,PublicNumberViewModel::class.java)
        val inflate = PublicNumberArticalListFragmentLayoutBinding
            .inflate(LayoutInflater.from(activity), container, false)
            .apply {
                lifecycleOwner = this@PublicArticalListFragment
            }
        inflate.viewModel = viewModel
        return inflate.root
    }

    override fun initData() {
        mCurrentId = getArguments()?.getInt("id")
        Log.i(_TAG, "mCurrentId:$mCurrentId")
        val listAdapter = PublicNumberListAdapter(activity as Context).apply {
            setOnItemClickListener {
                BlogDetailActivity.start(activity as Context, it.link)
            }
        }
        srf_artical_refresh?.setOnRefreshListener {
            mCurrentPage = 1
            loadData()
        }
        public_number_list?.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = listAdapter
            addItemDecoration(
                DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
            )
        }

        public_number_list?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //当前RecyclerView显示出来的最后一个的item的position
                var lastPosition: Int = -1
                //当前状态为停止滑动状态SCROLL_STATE_IDLE时
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    return
                }
                val layoutManager: RecyclerView.LayoutManager? = recyclerView.layoutManager
                lastPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                //时判断界面显示的最后item的position是否等于itemCount总数-1也就是倒数第三个item的position，开始预加载
                //如果相等则说明已经滑动到最后了
                if (lastPosition >= recyclerView.layoutManager!!.itemCount - 3) {
                    //防止多次重复加载
                    if (!viewModel.isPublicNumberDataListLoading) {
                        ++mCurrentPage
                        loadData()
                    }
                }
            }
        })

        viewModel.publicNumerArticalInfo.observe(this, Observer {
            it?.let {
                if (mCurrentPage == 1) {
                    listAdapter.resetDatas(it?.datas)
                    stateView.viewState = MultiStateView.VIEW_STATE_CONTENT
                }else{
                    listAdapter.addDatas(it?.datas)
                }
            }
        })

        viewModel?.error?.observe(this, Observer {
            //避免缓存加载下一页面的数据
            it?.let {
                stateView.viewState = MultiStateView.VIEW_STATE_ERROR
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            }
        })
        loadData()
    }

    override fun reload() {
        loadData()
    }

    fun loadData(){
        Log.i("mCurrentId", "mCurrentId:" + mCurrentId)
        viewModel.getPublicNumberDataList(mCurrentId ?: 408, mCurrentPage)
    }

}