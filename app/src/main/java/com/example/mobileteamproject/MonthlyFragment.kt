package com.example.mobileteamproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobileteamproject.databinding.FragmentMonthlyBinding

class MonthlyFragment : Fragment() {
    lateinit var binding:FragmentMonthlyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMonthlyBinding.inflate(layoutInflater, container, false)


        return binding.root
    }

}