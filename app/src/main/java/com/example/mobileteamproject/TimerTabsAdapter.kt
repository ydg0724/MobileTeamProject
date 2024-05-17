package com.example.mobileteamproject

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TimerTabsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> TimerTabTimerFragment()
            1 -> TimerTabStopwatchFragment()
            else -> throw IllegalStateException("Invalid adapter position")
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "타이머"
            1 -> "스톱워치"
            else -> null
        }
    }
}
