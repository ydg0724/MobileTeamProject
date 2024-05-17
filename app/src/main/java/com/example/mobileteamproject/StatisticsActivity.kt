package com.example.mobileteamproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileteamproject.databinding.ActivityStatisticsBinding
import com.google.android.material.tabs.TabLayout

class StatisticsActivity : AppCompatActivity() {
    lateinit var binding: ActivityStatisticsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "공부량 통계"
        //맨 처음 프래그먼트 화면
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_content, WeeklyFragment())
        transaction.commit()
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Toast.makeText(applicationContext, tab?.text, Toast.LENGTH_SHORT).show()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_content,MonthlyFragment())
                transaction.commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
//        //월간 공부량
//        binding.MonthlyBtn.setOnClickListener {
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragment_content,MonthlyFragment())
//            transaction.commit()
//        }
//        //주간 공부량
//        binding.WeeklyBtn.setOnClickListener {
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragment_content,WeeklyFragment())
//            transaction.commit()
//        }
    }
}