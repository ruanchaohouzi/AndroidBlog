package com.ruanchao.mvvmdemo.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.bean.*
import com.ruanchao.mvvmdemo.databinding.HomeRecyclerItemLayoutBinding
import com.ruanchao.mvvmdemo.ui.home.BlogDetailActivity
import com.ruanchao.mvvmdemo.ui.home.HomeBlogViewModel
import com.ruanchao.mvvmdemo.ui.login.LoginActivity
import kotlinx.android.synthetic.main.home_recycler_item_layout.view.*


class HomeBlogAdapter constructor(var mHomeDataList: MutableList<HomeData>, val mContext: Context,
                                  val viewModel: HomeBlogViewModel?): RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    fun addHomeDataList(data: MutableList<HomeData>){
        mHomeDataList.addAll(data)
        notifyDataSetChanged()
    }

    fun refreshHomeDataLists(data: MutableList<HomeData>){
        val bannerData: HomeData? = mHomeDataList[0]
        mHomeDataList.clear()
        if (bannerData != null) {
            mHomeDataList.add(bannerData)
        }
        mHomeDataList.addAll(data)
        notifyDataSetChanged()
    }

    fun setHomeDataLists(data: MutableList<HomeData>){
        mHomeDataList.clear()
        mHomeDataList.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (mHomeDataList.size == 0) 0 else mHomeDataList.size + 1
    }

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {

        if (viewHolder is ProjectViewHolder) {
            val dataInfo: DataInfo = mHomeDataList[position].itemValue as DataInfo
            viewHolder.bind(dataInfo){
                listener?.invoke(dataInfo)
            }
        }else if (viewHolder is BannerViewHolder){
            val banners: List<BannerInfo> = mHomeDataList[position].itemValue as List<BannerInfo>
            viewHolder.bind(mContext, banners)
        }
    }

    override fun onCreateViewHolder(container: ViewGroup, type: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {

        when(type){
            HomeData.VIEW_TYPE_BANNER_LIST ->{
                val view = LayoutInflater.from(mContext).inflate(R.layout.home_recycler_banner_item_layout, container, false)
                return BannerViewHolder(view)
            }
            HomeData.VIEW_TYPE_CONTENT ->{
                val itemBinding = HomeRecyclerItemLayoutBinding.inflate(
                    LayoutInflater.from(mContext), container, false)
                return ProjectViewHolder(itemBinding)
            }
            HomeData.VIEW_TYPE_FOOT -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.home_recycler_foot_item_layout, container, false)
                return FootViewHolder(view)
            }
            else ->{
               throw IllegalArgumentException()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == mHomeDataList.size) HomeData.VIEW_TYPE_FOOT else mHomeDataList[position].itemType
    }

    inner class ProjectViewHolder(var dataBinding: HomeRecyclerItemLayoutBinding)
        : RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(dataInfo: DataInfo, listener: ((DataInfo) -> Unit)?) {
            //进行数据绑定
            dataBinding.dataInfo = dataInfo
            dataBinding.root.setOnClickListener{
                listener?.invoke(dataInfo)
            }
            dataBinding.root.iv_collect.setOnClickListener {
                if (UserInfo.getInstance()?.user == null){
                    Toast.makeText(mContext, "请先登录", Toast.LENGTH_LONG).show()
                    LoginActivity.start(mContext as Activity)
                }else{
                    if (dataInfo.collect){
                        viewModel?.unCollectArtical(dataInfo)
                        dataBinding.root.iv_collect?.setImageResource(R.mipmap.artical_discollected)
                        dataInfo.collect = false
                    }else{
                        viewModel?.collectArtical(dataInfo)
                        dataBinding.root.iv_collect?.setImageResource(R.mipmap.artical_collected)
                        dataInfo.collect = true
                    }
//                    notifyDataSetChanged()
                }
            }
        }
    }

    class FootViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        val recyclerFootText: TextView = view.findViewById(R.id.tv_home_recycler_foot_view)

    }

    class BannerViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val mHomeSlider: SliderLayout = view.findViewById(R.id.home_slider)

        fun bind(context: Context, banners: List<BannerInfo>) {
            with(banners) {
                this.forEach { name ->
                    val textSliderView = TextSliderView(context)
                    // initialize a SliderLayout
                    textSliderView
                        .description(name.title)
                        .image(name.imagePath)
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener {
                            BlogDetailActivity.start(context, name.url)
                        }
                    mHomeSlider.addSlider(textSliderView)
                }
            }

            mHomeSlider.setPresetTransformer(SliderLayout.Transformer.Default)
            mHomeSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
            //mHomeSlider.setCustomAnimation(Default())
            mHomeSlider.setDuration(4000)
            mHomeSlider.addOnPageChangeListener(object : ViewPagerEx.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                }
            })
        }
    }

    private var listener: ((DataInfo) -> Unit)? = null

    fun setOnItemClickListener(listener: ((DataInfo) -> Unit)){
        this.listener = listener
    }

    fun getHomeDataList() = mHomeDataList
}