package com.ruanchao.mvvmdemo.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter

class PublicNumberAdapter(val fragments:List<Fragment>,val fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int = fragments.size

    override fun getItem(p0: Int): Fragment  = fragments.get(p0)

}