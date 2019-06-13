package com.ruanchao.mvvmdemo.ui.knowledge

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruanchao.mvpframe.utils.StatusBarUtil
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.adapter.KnowledgeAdapter
import com.ruanchao.mvvmdemo.databinding.KnowledgeFragmentBinding
import com.ruanchao.mvvmdemo.ui.base.BaseFragment
import com.ruanchao.mvvmdemo.utils.obtainViewModel
import com.ruanchao.mvvmdemo.view.MultiStateView
import kotlinx.android.synthetic.main.knowledge_fragment.*

class KnowledgeFragment: BaseFragment() {

    lateinit var mKnowledgeAdapter: KnowledgeAdapter
    lateinit var mViewModel: KnowledgeViewModel

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val inflate = KnowledgeFragmentBinding.inflate(inflater, container, false)
        return inflate.root
    }

    override fun initData() {
        StatusBarUtil.immersive(activity as Activity, activity!!.getColor(R.color.public_number_tab_bg))
        StatusBarUtil.setPaddingSmart(activity as Activity, mKnowledgeToolbar)
        mViewModel = obtainViewModel(this, KnowledgeViewModel::class.java)
        mKnowledgeAdapter = KnowledgeAdapter()
        mKnowledgeAdapter.setEnableLoadMore(false)
        with(knowledgeRecyclerView){
            layoutManager = LinearLayoutManager(getContext())
            adapter = mKnowledgeAdapter
        }
        mViewModel.knowledgeInfoList.observe(this, Observer {
            it?.let {
                mKnowledgeAdapter.setNewData(it)
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
    }

}