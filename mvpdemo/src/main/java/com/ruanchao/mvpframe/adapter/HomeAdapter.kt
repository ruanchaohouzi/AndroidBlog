package com.ruanchao.mvpframe.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.daimajia.slider.library.SliderLayout
import com.ruanchao.mvpframe.R
import com.ruanchao.mvpframe.bean.HomeData
import com.ruanchao.mvpframe.bean.ProjectInfo
import android.widget.Toast
import com.ruanchao.mvpframe.MainActivity
import android.widget.AdapterView
import com.daimajia.slider.library.Animations.DescriptionAnimation
import android.os.Bundle
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import android.R.attr.description
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import com.ruanchao.mvpframe.bean.BannerInfo


class HomeAdapter constructor(data: MutableList<HomeData>, context: Context): androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    var mHomeDataList: MutableList<HomeData> = mutableListOf()
    var mContext: Context? = null;

    init {
        mHomeDataList = data
        mContext = context
    }

    fun addHomeDataList(data: MutableList<HomeData>){
        mHomeDataList.addAll(data)
    }

    fun refreshHomeDataLists(data: MutableList<HomeData>){
        val bannerData: HomeData? = mHomeDataList.get(0);
        mHomeDataList.clear()
        if (bannerData != null) {
            mHomeDataList.add(bannerData)
        }
        mHomeDataList.addAll(data)
    }

    fun setHomeDataLists(data: MutableList<HomeData>){
        mHomeDataList.clear()
        mHomeDataList.addAll(data)
    }

    fun getHomeDataList() = mHomeDataList

    override fun getItemCount(): Int {
        return if (mHomeDataList.size == 0) 0 else mHomeDataList.size + 1
    }

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {

        if (viewHolder is ProjectViewHolder) {
            bindProjectViewHolder(viewHolder, position)
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
                val view = LayoutInflater.from(mContext).inflate(R.layout.home_recycler_item_layout, container, false)
                return ProjectViewHolder(view)
            }
            HomeData.VIEW_TYPE_FOOT -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.home_recycler_foot_item_layout, container, false)
                return FootViewHolder(view)
            }
            else ->{
                val view = LayoutInflater.from(mContext).inflate(R.layout.home_recycler_item_layout, container, false)
                return ProjectViewHolder(view)
            }
        }
    }

    private fun bindProjectViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        var mProjectViewHolder: ProjectViewHolder = viewHolder as ProjectViewHolder
        var projectInfo: ProjectInfo = mHomeDataList!!.get(position).itemValue as ProjectInfo
        mProjectViewHolder.mTitleView.setText(projectInfo.title)
        mProjectViewHolder.mAuthor.setText(projectInfo.author)
        mProjectViewHolder.mCategory.setText(projectInfo.superChapterName)
        mProjectViewHolder.mNiceDate.setText(projectInfo.niceDate)
        mProjectViewHolder?.itemViewLayout?.setOnClickListener(View.OnClickListener {
            if (mOnItemClickListener != null) {
                mOnItemClickListener(position, projectInfo)
            }
        })
    }

    private fun bindBannerViewHolder(position: Int, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder) {
        var banners: List<BannerInfo> = mHomeDataList!!.get(position).itemValue as List<BannerInfo>;
        var bannerViewHolder: BannerViewHolder = viewHolder as BannerViewHolder
        var mHomeSlider = bannerViewHolder.mHomeSlider
        for (name in banners) {
            val textSliderView = TextSliderView(mContext)
            // initialize a SliderLayout
            textSliderView
                .description(name.title)
                .image(name.imagePath)
                .setScaleType(BaseSliderView.ScaleType.Fit)
                .setOnSliderClickListener(object : BaseSliderView.OnSliderClickListener {
                    override fun onSliderClick(slider: BaseSliderView?) {

                    }

                })
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
        return if(position == mHomeDataList.size) HomeData.VIEW_TYPE_FOOT else mHomeDataList.get(position).itemType
    }

    lateinit var mOnItemClickListener: (Int, ProjectInfo) -> Unit

    fun setOnItemClickListener(onItemClickListener: (Int, ProjectInfo)-> Unit){
        mOnItemClickListener = onItemClickListener
    }

    class ProjectViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        val itemViewLayout: View = view
        val mTitleView: TextView
        val mAuthor: TextView
        val mCategory: TextView
        val mNiceDate: TextView

        init {
            mTitleView = view.findViewById<TextView>(R.id.projectTitle)
            mAuthor = view.findViewById<TextView>(R.id.tv_Author)
            mCategory = view.findViewById<TextView>(R.id.tv_category_value)
            mNiceDate = view.findViewById<TextView>(R.id.tv_niceDate)
        }

    }

    class FootViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        val recyclerFootText: TextView

        init {
            recyclerFootText = view.findViewById<TextView>(R.id.tv_home_recycler_foot_view)
        }
    }

    class BannerViewHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val mHomeSlider: SliderLayout
        init {
            mHomeSlider = view.findViewById<SliderLayout>(R.id.home_slider)
        }
    }
}