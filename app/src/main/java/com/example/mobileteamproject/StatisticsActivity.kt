package com.example.mobileteamproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileteamproject.databinding.ActivityStatisticsBinding

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

        binding.MonthlyBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_content,MonthlyFragment())
            transaction.commit()
        }
        //주간 공부량
            binding.WeeklyBtn.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_content,WeeklyFragment())
            transaction.commit()
        }
    }
}