package com.ruanchao.mvvmdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.daimajia.slider.library.SliderLayout
import com.ruanchao.mvvmdemo.bean.HomeData
import com.ruanchao.mvvmdemo.bean.ProjectInfo
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import androidx.recyclerview.widget.RecyclerView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.ruanchao.mvvmdemo.bean.BannerInfo
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.databinding.HomeRecyclerItemLayoutBinding
import com.ruanchao.mvvmdemo.ui.home.BlogDetailActivity
import com.ruanchao.mvvmdemo.ui.home.HomeBlogViewModel


class HomeBlogAdapter constructor(data: MutableList<HomeData>, context: Context,
                                  viewModel: HomeBlogViewModel?): androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    private var mHomeDataList: MutableList<HomeData> = mutableListOf()
    var mContext: Context? = null

    init {
        mHomeDataList = data
        mContext = context
    }

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
            val projectInfo: ProjectInfo = mHomeDataList[position].itemValue as ProjectInfo
            viewHolder.bind(projectInfo)

            viewHolder.dataBinding.root.setOnClickListener{
                listener?.invoke(projectInfo)
            }
        }else if (viewHolder is BannerViewHolder){
            bindBannerViewHolder(position, viewHolder)
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
                    LayoutInflater.from(mContext),
                    container, false)
                itemBinding.projectInfo
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

    private fun bindBannerViewHolder(position: Int, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        val banners: List<BannerInfo> = mHomeDataList[position].itemValue as List<BannerInfo>
        val bannerViewHolder: BannerViewHolder = viewHolder as BannerViewHolder
        val mHomeSlider = bannerViewHolder.mHomeSlider
        for (name in banners) {
            val textSliderView = TextSliderView(mContext)
            // initialize a SliderLayout
            textSliderView
                .description(name.title)
                .image(name.imagePath)
                .setScaleType(BaseSliderView.ScaleType.Fit)
                .setOnSliderClickListener{
                    BlogDetailActivity.start(mContext!!,name.url)
                }
            mHomeSlider.addSlider(textSliderView)
        }
        mHomeSlider.setPresetTransformer(SliderLayout.Transformer.Default)
        mHomeSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
//        mHomeSlider.setCustomAnimation(Default())
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

    override fun getItemViewType(position: Int): Int {
        return if(position == mHomeDataList.size) HomeData.VIEW_TYPE_FOOT else mHomeDataList[position].itemType
    }

    private lateinit var mOnItemClickListener: (Int, ProjectInfo) -> Unit

    fun setOnItemClickListener(onItemClickListener: (Int, ProjectInfo)-> Unit){
        mOnItemClickListener = onItemClickListener
    }

    class ProjectViewHolder(var dataBinding: HomeRecyclerItemLayoutBinding)
        : androidx.recyclerview.widget.RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(projectInfo: ProjectInfo) {
            dataBinding.projectInfo = projectInfo
        }
    }

    class FootViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        val recyclerFootText: TextView = view.findViewById(R.id.tv_home_recycler_foot_view)

    }

    class BannerViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val mHomeSlider: SliderLayout = view.findViewById(R.id.home_slider)
    }

    private var listener: ((ProjectInfo) -> Unit)? = null

    fun setOnItemClickListener(listener: ((ProjectInfo) -> Unit)){
        this.listener = listener
    }

    fun getHomeDataList() = mHomeDataList
}