package com.ruanchao.androidblog.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PublicNumberAdapter(val fragments:List<Fragment>, val fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment = fragments.get(position)

    override fun getCount(): Int = fragments.size

}