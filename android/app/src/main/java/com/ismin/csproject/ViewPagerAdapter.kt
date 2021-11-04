package com.ismin.csproject

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter

class ViewPagerAdapter(private val fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitle = ArrayList<String>()

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTitle[position]
    }

    fun addFragment(f:Fragment, title:String) {
        fragmentList.add(f);
        fragmentTitle.add(title);
    }

    fun deleteFragments() {
        fragmentList.clear()
        fragmentTitle.clear()
    }
}