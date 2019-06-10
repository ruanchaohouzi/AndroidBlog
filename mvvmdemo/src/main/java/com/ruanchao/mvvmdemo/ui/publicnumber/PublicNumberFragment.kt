package com.ruanchao.mvvmdemo.ui.publicnumber

import android.app.Activity
import androidx.lifecycle.Observer
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ruanchao.mvvmdemo.bean.PublicNumberInfo
import com.ruanchao.mvvmdemo.databinding.PublicNumberFragmentLayoutBinding
import com.ruanchao.mvvmdemo.utils.obtainViewModel
import kotlinx.android.synthetic.main.public_number_fragment_layout.*
import com.ruanchao.mvpframe.utils.StatusBarUtil
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.adapter.PublicNumberAdapter
import com.google.android.material.tabs.TabLayout


class PublicNumberFragment: Fragment() {

    lateinit var mViewBinding: PublicNumberFragmentLayoutBinding
    var fragments = mutableListOf<Fragment>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewBinding = PublicNumberFragmentLayoutBinding.inflate(inflater, container, false).apply {
            viewModel = obtainViewModel(this@PublicNumberFragment, PublicNumberViewModel::class.java)
            lifecycleOwner = this@PublicNumberFragment
        }
        initView(mViewBinding.root)
        return mViewBinding.root
    }

    private fun initView(root: View) {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        StatusBarUtil.immersive(activity as Activity, activity!!.getColor(R.color.public_number_tab_bg))
        StatusBarUtil.setPaddingSmart(activity as Activity, tab)
        mViewBinding?.viewModel?.getPublicNumberList()
        mViewBinding?.viewModel?.publicNumberInfos?.observe(this, Observer {
            if(it != null) {
                setTab(it)
            }
        })

        mViewBinding?.viewModel?.error?.observe(this, Observer {
            if (!TextUtils.isEmpty(it)) {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun setTab(publicNumberInfos: List<PublicNumberInfo>){
        for (i in 0 until publicNumberInfos.size){
            tab.addTab(tab.newTab().setText(publicNumberInfos[i].name));
            fragments.add(PublicArticalListFragment.newInstance(publicNumberInfos[i].id))
        }

        with(viewPager){
            var adapter = PublicNumberAdapter(fragments, this@PublicNumberFragment)
            setAdapter(adapter)
            val recycler = getChildAt(0) as RecyclerView
            //关闭离屏加载的预加载缓存
            recycler.layoutManager?.isItemPrefetchEnabled = false
            //关闭Recycler自带的item缓存
            recycler.setItemViewCacheSize(0)

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tab.setScrollPosition(position,0f, false)
                }
            })
        }
        tab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
                viewPager.currentItem = p0?.position ?: 0
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
            }

        })
    }

}
