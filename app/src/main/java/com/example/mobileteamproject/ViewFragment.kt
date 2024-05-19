package com.example.mobileteamproject

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobileteamproject.databinding.FragmentViewBinding

class ViewFragment : Fragment() {
    lateinit var binding: FragmentViewBinding
    var data: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getString("data")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewBinding.inflate(inflater, container, false)
        binding.textview.text = data
        binding.textview.movementMethod = ScrollingMovementMethod.getInstance()
        return binding.root
    }
}