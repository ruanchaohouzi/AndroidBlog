package com.ruanchao.mvvmdemo.ui.knowledge

import androidx.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.ruanchao.mvvmdemo.adapter.PublicNumberListAdapter
import com.ruanchao.mvvmdemo.databinding.KnowledgeChildListFragmentLayoutBinding
import com.ruanchao.mvvmdemo.ui.base.BaseFragment
import com.ruanchao.mvvmdemo.ui.home.BlogDetailActivity
import com.ruanchao.mvvmdemo.utils.obtainViewModel
import com.ruanchao.mvvmdemo.view.MultiStateView
import kotlinx.android.synthetic.main.knowledge_child_list_fragment_layout.*


class KnowledgeChildListFragment: BaseFragment() {

    lateinit var viewModel: KnowledgeViewModel
    var mCurrentPage: Int = 0
    var mCurrentId: Int? = 408;
    private val _TAG = KnowledgeChildListFragment::class.java.simpleName

    companion object {
        fun newInstance(id: Int, name: String): KnowledgeChildListFragment {
            var f = KnowledgeChildListFragment()
            var bundle = Bundle()
            bundle.putInt("id", id)
            bundle.putString("name", name)
            f.arguments = bundle
            return f
        }
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = obtainViewModel(this,KnowledgeViewModel::class.java)
        val inflate = KnowledgeChildListFragmentLayoutBinding
            .inflate(LayoutInflater.from(activity), container, false)
            .apply {
                lifecycleOwner = this@KnowledgeChildListFragment
            }
        inflate.viewModel = viewModel
        return inflate.root
    }

    override fun initData() {
        mHomeTitle.text = getArguments()?.getString("name")
        mCurrentId = getArguments()?.getInt("id")
        Log.i(_TAG, "mCurrentId:$mCurrentId")
        val listAdapter = PublicNumberListAdapter(activity as Context, viewModel){
            BlogDetailActivity.start(activity as Context, it.link)
        }
        srf_artical_refresh?.setOnRefreshListener {
            mCurrentPage = 0
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
                    if (!viewModel.isKnowledgeChildDataListLoading) {
                        ++mCurrentPage
                        loadData()
                    }
                }
            }
        })

        viewModel.knowledgeChildDataInfo.observe(this, Observer {
            it?.let {
                if (mCurrentPage == 0) {
                    listAdapter.resetDatas(it?.datas)
                    stateView.viewState = MultiStateView.VIEW_STATE_CONTENT
                }else{
                    listAdapter.addDatas(it?.datas)
                }
            }
        })

        viewModel?.knowledgeChildError?.observe(this, Observer {
            it?.let {
                stateView.viewState = MultiStateView.VIEW_STATE_ERROR
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            }
        })

        viewModel?.collectErrInfo?.observe(this, Observer {
            it?.let {
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
        viewModel.getKnowledgeChildList(mCurrentPage, mCurrentId ?: 60)
    }

}