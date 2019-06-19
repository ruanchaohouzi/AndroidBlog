package com.ruanchao.mvvmdemo.adapter

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.flexbox.*
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.bean.Children
import com.ruanchao.mvvmdemo.bean.KnowledgeInfo
import com.ruanchao.mvvmdemo.ui.knowledge.ChildKnowledgeActivity

class KnowledgeAdapter: BaseQuickAdapter<KnowledgeInfo, BaseViewHolder>(R.layout.rv_item_knowledge){

    override fun convert(helper: BaseViewHolder?, item: KnowledgeInfo?) {
        helper?.setText(R.id.tv_name, item?.name)

        val knowledegChildRecycler = helper?.getView<RecyclerView>(R.id.knowledge_child_recycler)
        var flexboxLayoutManager = FlexboxLayoutManager(mContext).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            alignItems = AlignItems.STRETCH
            justifyContent = JustifyContent.FLEX_START
        }
        var mKnowledgeChildAdapter = KnowledgeChildAdapter()
        knowledegChildRecycler?.apply {
            layoutManager = flexboxLayoutManager
            adapter = mKnowledgeChildAdapter
        }
        mKnowledgeChildAdapter.setNewData(item?.children)

    }

    class KnowledgeChildAdapter : BaseQuickAdapter<Children, BaseViewHolder>(R.layout.rv_item_knowledge_child) {
        override fun convert(helper: BaseViewHolder?, item: Children?) {
            helper?.setText(R.id.tv_name, item?.name)
            helper?.itemView?.setOnClickListener{
                ChildKnowledgeActivity.start(mContext, item?.id, item?.name)
            }
        }

    }

}