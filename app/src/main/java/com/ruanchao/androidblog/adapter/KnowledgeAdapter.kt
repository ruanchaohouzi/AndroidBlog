package com.ruanchao.androidblog.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.flexbox.*
import com.ruanchao.androidblog.R
import com.ruanchao.androidblog.bean.Children
import com.ruanchao.androidblog.bean.KnowledgeInfo
import com.ruanchao.androidblog.ui.knowledge.ChildKnowledgeActivity

class KnowledgeAdapter(val mContext: Context, val recyclerViewPool: RecyclerView.RecycledViewPool) : BaseQuickAdapter<KnowledgeInfo, BaseViewHolder>(R.layout.rv_item_knowledge){

    override fun convert(helper: BaseViewHolder, item: KnowledgeInfo) {
                helper?.setText(R.id.tv_name, item?.name)

        val knowledegChildRecycler = helper?.getView<RecyclerView>(R.id.knowledge_child_recycler)
        var flexboxLayoutManager = FlexboxLayoutManager(mContext).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            alignItems = AlignItems.STRETCH
            justifyContent = JustifyContent.FLEX_START
        }
        var mKnowledgeChildAdapter = KnowledgeChildAdapter(mContext)
        knowledegChildRecycler?.apply {
            layoutManager = flexboxLayoutManager
            adapter = mKnowledgeChildAdapter
        }
//        knowledegChildRecycler?.setRecycledViewPool(recyclerViewPool)
        mKnowledgeChildAdapter.setNewData(item?.children as MutableList<Children>)
    }

    class KnowledgeChildAdapter(val mContext: Context) : BaseQuickAdapter<Children, BaseViewHolder>(R.layout.rv_item_knowledge_child)
    {
        override fun convert(helper: BaseViewHolder, item: Children) {
                        helper?.setText(R.id.tv_name, item?.name)
            helper?.itemView?.setOnClickListener{
                ChildKnowledgeActivity.start(mContext, item?.id, item?.name)
            }
        }
    }

}