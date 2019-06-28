package com.multifunctional.reminder.captain.ki.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.multifunctional.reminder.captain.ki.fragments.TimerFragment
import java.util.*

class ScreenSlidePagerAdapter(
        private val fragmentList: ArrayList<Fragment>,
        fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int = fragmentList.size

    override fun getItem(position: Int): Fragment {
        if (position >= 0 && position < fragmentList.size)
            return fragmentList[position]
        return TimerFragment()
    }
}