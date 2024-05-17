package com.example.mobileteamproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class TimerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        val viewPager: ViewPager = view.findViewById(R.id.viewPager)
        val tabs: TabLayout = view.findViewById(R.id.tabLayout)

        // ViewPager와 TabLayout을 연결
        val adapter = TimerTabsAdapter(childFragmentManager)
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        return view
    }
}