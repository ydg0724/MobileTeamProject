package com.example.mobileteamproject

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobileteamproject.databinding.FragmentEditBinding

class EditFragment : Fragment() {
    lateinit var binding: FragmentEditBinding
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
        binding = FragmentEditBinding.inflate(inflater, container, false)
        binding.editview.setText(data)
        binding.button.setOnClickListener {
            val mActivity = activity as BookNoteActivity
            mActivity.receiveData(binding.editview.text.toString())
        }
        return binding.root
    }
}