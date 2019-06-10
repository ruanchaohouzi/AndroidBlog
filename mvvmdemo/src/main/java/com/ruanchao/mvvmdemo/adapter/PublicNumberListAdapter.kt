package com.ruanchao.mvvmdemo.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ruanchao.mvvmdemo.bean.DataInfo
import com.ruanchao.mvvmdemo.databinding.PublicNumberArticalRecyclerItemBinding
import com.ruanchao.mvvmdemo.ui.publicnumber.PublicNumberViewModel

class PublicNumberListAdapter(var context: Context, var viewModel: PublicNumberViewModel) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    var datas: MutableList<DataInfo> = mutableListOf()

    fun resetDatas(datas: List<DataInfo>?){
        if (datas != null) {
            this.datas = datas as MutableList<DataInfo>
            notifyDataSetChanged()
        }
    }

    fun addDatas(datas: List<DataInfo>?) {
        if (datas != null) {
            this.datas.addAll(datas as MutableList<DataInfo>)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {

        val inflate = PublicNumberArticalRecyclerItemBinding.inflate(LayoutInflater.from(context), p0, false)
        return ItemViewHolder(inflate)
    }

    override fun getItemCount() = datas.size


    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        viewHolder as ItemViewHolder
        viewHolder.dataBingding.dataInfo = datas[position]
        viewHolder.dataBingding.root.setOnClickListener{
            mOnItemClickListener?.invoke(datas[position])
        }
    }

    class ItemViewHolder(val dataBingding: PublicNumberArticalRecyclerItemBinding): androidx.recyclerview.widget.RecyclerView.ViewHolder(dataBingding.root) {

    }

    var mOnItemClickListener: ((dataInfo: DataInfo) ->Unit)? = null

    fun setOnItemClickListener(listener:(dataInfo: DataInfo) ->Unit){
        mOnItemClickListener = listener
    }

}