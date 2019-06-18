package com.ruanchao.mvvmdemo.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.bean.DataInfo

class PublicNumberListAdapter(var context: Context,
                              var listener:((dataInfo: DataInfo) ->Unit)?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {

        val inflate = LayoutInflater.from(context).inflate(R.layout.public_number_artical_recycler_item, p0, false)
        return ItemViewHolder(inflate)
    }

    override fun getItemCount() = datas.size


    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        viewHolder as ItemViewHolder
        viewHolder.bindItemView(datas[position], listener)
    }

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var tv_Author: TextView? = null
        var tv_niceDate: TextView? = null
        var tv_title: TextView? = null
        var tv_chapterName: TextView? = null
        init {
            tv_Author = itemView.findViewById(R.id.tv_Author)
            tv_niceDate = itemView.findViewById(R.id.tv_niceDate)
            tv_title = itemView.findViewById(R.id.tv_title)
            tv_chapterName = itemView.findViewById(R.id.tv_chapterName)
        }

        //直接在ViewHolder里面绑定数据
        fun bindItemView(dataInfo: DataInfo, listener: ((dataInfo: DataInfo) ->Unit)?){
            with(dataInfo){
                tv_Author?.text = author
                tv_niceDate?.text = niceDate
                tv_title?.text = title
                tv_chapterName?.text = chapterName
                itemView.setOnClickListener{
                    listener?.invoke(dataInfo)
                }
            }
        }
    }

}