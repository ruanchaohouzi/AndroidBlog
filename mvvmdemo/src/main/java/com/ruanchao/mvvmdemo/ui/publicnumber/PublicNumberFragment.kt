package com.ruanchao.mvvmdemo.ui.publicnumber

import android.app.Activity
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ruanchao.mvvmdemo.bean.PublicNumberInfo
import com.ruanchao.mvvmdemo.databinding.PublicNumberFragmentLayoutBinding
import com.ruanchao.mvvmdemo.utils.obtainViewModel
import kotlinx.android.synthetic.main.public_number_fragment_layout.*
import com.ruanchao.mvpframe.utils.StatusBarUtil
import com.ruanchao.mvvmdemo.R
import com.ruanchao.mvvmdemo.adapter.PublicNumberAdapter


class PublicNumberFragment: Fragment() {

    var mViewBinding: PublicNumberFragmentLayoutBinding? = null

    var fragments = mutableListOf<Fragment>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewBinding = PublicNumberFragmentLayoutBinding.inflate(inflater, container, false).apply {
            viewModel = obtainViewModel(this@PublicNumberFragment, PublicNumberViewModel::class.java)
            lifecycleOwner = this@PublicNumberFragment
        }

        return mViewBinding?.root
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
    }

    fun setTab(publicNumberInfos: List<PublicNumberInfo>){
        for (i in 0 until publicNumberInfos.size){
            tab.addTab(tab.newTab());
            fragments.add(PublicArticalListFragment.newInstance(publicNumberInfos[i].id))
        }

        tab.setupWithViewPager(viewPager, false)
        var adapter = PublicNumberAdapter(fragments, childFragmentManager)
        viewPager.setAdapter(adapter)

        for (i in 0 until publicNumberInfos.size) {
            tab.getTabAt(i)?.setText(publicNumberInfos[i].name)
        }
    }

}
