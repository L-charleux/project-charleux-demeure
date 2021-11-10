package com.ismin.csproject

import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import java.util.*
import kotlin.collections.ArrayList

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
        fragmentList.add(fragmentList.size, f)
        fragmentTitle.add(fragmentTitle.size,title)
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        fragmentList.removeAt(position)
        fragmentTitle.removeAt(position)
        //notifyDataSetChanged()
        Log.d("TAG", Date().toString())
    }

}