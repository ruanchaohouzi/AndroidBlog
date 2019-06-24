package com.ruanchao.androidblog.ui.publicnumber

import android.app.Activity
import androidx.lifecycle.Observer
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.ruanchao.androidblog.bean.PublicNumberInfo
import com.ruanchao.androidblog.databinding.PublicNumberFragmentLayoutBinding
import com.ruanchao.androidblog.utils.obtainViewModel
import kotlinx.android.synthetic.main.public_number_fragment_layout.*
import com.ruanchao.mvpframe.utils.StatusBarUtil
import com.ruanchao.androidblog.R
import com.ruanchao.androidblog.adapter.PublicNumberAdapter
import com.google.android.material.tabs.TabLayout
import com.ruanchao.androidblog.ui.base.BaseFragment
import com.ruanchao.androidblog.view.MultiStateView


class PublicNumberFragment: BaseFragment() {

    private val _TAG = PublicNumberFragment::class.java.simpleName

    lateinit var mViewBinding: PublicNumberFragmentLayoutBinding
    var fragments = mutableListOf<Fragment>()

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mViewBinding = PublicNumberFragmentLayoutBinding.inflate(inflater, container, false).apply {
            viewModel = obtainViewModel(this@PublicNumberFragment, PublicNumberViewModel::class.java)
            lifecycleOwner = this@PublicNumberFragment
        }
        return mViewBinding.root
    }

    override fun initData() {
        StatusBarUtil.immersive(activity as Activity, activity!!.getColor(R.color.public_number_tab_bg))
        StatusBarUtil.setPaddingSmart(activity as Activity, tab)

        mViewBinding?.viewModel?.getPublicNumberList()
        mViewBinding?.viewModel?.publicNumberInfos?.observe(this, Observer {

            if(it != null) {
                setTab(it)
                stateView.viewState = MultiStateView.VIEW_STATE_CONTENT
            }
        })

        mViewBinding?.viewModel?.error?.observe(this, Observer {
            if (!TextUtils.isEmpty(it)) {
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
                stateView.viewState = MultiStateView.VIEW_STATE_ERROR
            }
        })
    }

    override fun reload() {
        mViewBinding?.viewModel?.getPublicNumberList()
    }

    fun setTab(publicNumberInfos: List<PublicNumberInfo>){
        for (i in 0 until publicNumberInfos.size){
            tab.addTab(tab.newTab().setText(publicNumberInfos[i].name));
            fragments.add(PublicArticalListFragment.newInstance(publicNumberInfos[i].id))
        }

        with(viewPager){
            var adapter = PublicNumberAdapter(fragments, childFragmentManager)
            setAdapter(adapter)
            addOnPageChangeListener(
                object : ViewPager.OnPageChangeListener {
                    override fun onPageScrollStateChanged(state: Int) {
                    }

                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    }

                    override fun onPageSelected(position: Int) {
                        tab.setScrollPosition(position,0f, false)
                    }

                }
            )
        }

        tab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                Log.i(_TAG,"page:" + p0?.position)

                viewPager.currentItem = p0?.position ?: 0
            }

        })
    }

}
