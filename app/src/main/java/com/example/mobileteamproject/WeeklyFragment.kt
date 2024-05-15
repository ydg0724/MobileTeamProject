package com.example.mobileteamproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobileteamproject.databinding.FragmentWeeklyBinding


class WeeklyFragment : Fragment() {
    lateinit var binding : FragmentWeeklyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeeklyBinding.inflate(inflater, container, false)


        return binding.root
    }

}