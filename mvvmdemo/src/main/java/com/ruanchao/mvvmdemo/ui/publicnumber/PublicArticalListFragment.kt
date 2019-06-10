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
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.adapter.PublicNumberListAdapter
import com.ruanchao.mvvmdemo.databinding.PublicNumberArticalListFragmentLayoutBinding
import com.ruanchao.mvvmdemo.ui.home.BlogDetailActivity
import com.ruanchao.mvvmdemo.utils.obtainViewModel
import com.ruanchao.mvvmdemo.view.LazyLoadFragment


class PublicArticalListFragment: Fragment() {

    lateinit var viewModel: PublicNumberViewModel
    var public_number_list: RecyclerView? = null
    var mRefresh: SwipeRefreshLayout? = null
    var mCurrentPage: Int = 1
    var mCurrentId: Int? = 408;

    companion object {
        fun newInstance(id: Int): PublicArticalListFragment {
            var f = PublicArticalListFragment()
            var bundle = Bundle()
            bundle.putInt("id", id)
            f.arguments = bundle
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = (activity as AppCompatActivity).obtainViewModel(PublicNumberViewModel::class.java)
        val inflate = PublicNumberArticalListFragmentLayoutBinding
            .inflate(LayoutInflater.from(activity), container, false)
            .apply {
                lifecycleOwner = this@PublicArticalListFragment
            }
        inflate.viewModel = viewModel
        init(inflate.root)
        return inflate.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadData()
    }


    private fun init(view: View) {
        mCurrentId = getArguments()?.getInt("id")
        val listAdapter = PublicNumberListAdapter(activity as Context, viewModel).apply {
            setOnItemClickListener {
                BlogDetailActivity.start(activity as Context, it.link)
            }
        }
        mRefresh = view.findViewById(R.id.srf_artical_refresh)
        mRefresh?.setOnRefreshListener {
            mCurrentPage = 1
            loadData()
        }
        public_number_list = view!!.findViewById(R.id.public_number_list)
        public_number_list?.run {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)
            adapter = listAdapter
            addItemDecoration(
                androidx.recyclerview.widget.DividerItemDecoration(
                    activity,
                    androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
                )
            )
        }

        public_number_list?.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //当前RecyclerView显示出来的最后一个的item的position
                var lastPosition: Int = -1
                //当前状态为停止滑动状态SCROLL_STATE_IDLE时
                if (newState != androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE) {
                    return
                }
                val layoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = recyclerView.layoutManager
                lastPosition = (layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
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
            if (mCurrentPage == 1) {
                listAdapter.resetDatas(it?.datas)
            }else{
                listAdapter.addDatas(it?.datas)
            }
//            if (it != null) {
//                mLoadStateView?.viewState = MultiStateView.STATE_CONTENT
//            }
        })

        viewModel?.error?.observe(this, Observer {
            if (!TextUtils.isEmpty(it)) {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
//                mLoadStateView?.viewState = MultiStateView.STATE_FAIL
            }
        })
    }

    fun loadData(){
        Log.i("mCurrentId", "mCurrentId:" + mCurrentId)
        viewModel.getPublicNumberDataList(mCurrentId ?: 408, mCurrentPage)
    }

}