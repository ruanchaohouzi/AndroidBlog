package com.ruanchao.androidblog.ui.knowledge

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ruanchao.mvpframe.utils.StatusBarUtil
import com.ruanchao.androidblog.R
import com.ruanchao.androidblog.adapter.KnowledgeAdapter
import com.ruanchao.androidblog.bean.KnowledgeInfo
import com.ruanchao.androidblog.databinding.KnowledgeFragmentBinding
import com.ruanchao.androidblog.ui.base.BaseFragment
import com.ruanchao.androidblog.utils.obtainViewModel
import com.ruanchao.androidblog.view.MultiStateView
import kotlinx.android.synthetic.main.knowledge_fragment.*

class KnowledgeFragment: BaseFragment() {

    lateinit var mKnowledgeAdapter: KnowledgeAdapter
    lateinit var mViewModel: KnowledgeViewModel

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val inflate = KnowledgeFragmentBinding.inflate(inflater, container, false)
        return inflate.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        StatusBarUtil.immersive(activity as Activity, activity!!.getColor(R.color.public_number_tab_bg))
        StatusBarUtil.setPaddingSmart(activity as Activity, mKnowledgeToolbar)
        mViewModel = obtainViewModel(this, KnowledgeViewModel::class.java)
        val recyclerViewPool = RecyclerView.RecycledViewPool()
        mKnowledgeAdapter = KnowledgeAdapter(activity as Context,recyclerViewPool)
        with(knowledgeRecyclerView){
            layoutManager = LinearLayoutManager(getContext())
            adapter = mKnowledgeAdapter
        }
        knowledgeRecyclerView.setRecycledViewPool(recyclerViewPool)
        mViewModel.knowledgeInfoList.observe(this, Observer {
            it?.let {
                mKnowledgeAdapter.setNewData(it as MutableList<KnowledgeInfo>)
                stateView.viewState = MultiStateView.VIEW_STATE_CONTENT
            }
        })

        mViewModel.errInfo.observe(this, Observer {
            it?.let {
                Toast.makeText(activity,it, Toast.LENGTH_LONG).show()
                stateView.viewState = MultiStateView.VIEW_STATE_ERROR
            }
        })
        mViewModel.getKnowledgeList()
    }

    override fun reload() {
        stateView.viewState = MultiStateView.VIEW_STATE_LOADING
        mViewModel.getKnowledgeList()
    }

}