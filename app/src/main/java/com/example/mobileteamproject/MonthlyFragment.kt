package com.example.mobileteamproject

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobileteamproject.databinding.FragmentMonthlyBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

class MonthlyFragment : Fragment() {
    lateinit var binding:FragmentMonthlyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMonthlyBinding.inflate(layoutInflater, container, false)
        
        val db = requireContext().openOrCreateDatabase("studydb", MODE_PRIVATE, null)
        //월별 데이터 추출 쿼리문
        val query = """
        SELECT strftime('%Y-%m', DATE) AS Month, SUM(studyTime) AS TotalTime
        FROM STUDY_TB
        GROUP BY strftime('%Y-%m', DATE)
        ORDER BY DATE ASC
    """.trimIndent()

        var monthTotalTime:Double = 0.0 //총 공부량
        val dateList = mutableListOf<String>()
        val studyTimeList = mutableListOf<Double>()
        val cursor = db.rawQuery(query,null)
        //월별 데이터 뽑기
        cursor.use {
            while(it.moveToNext()){
                val month = it.getString(it.getColumnIndexOrThrow("Month"))
                val totalTime = it.getDouble(it.getColumnIndexOrThrow("TotalTime"))
                dateList.add(month)
                studyTimeList.add(totalTime)
                monthTotalTime += totalTime
            }
        }
        val monthTotalTimeInt = monthTotalTime.toInt()
        val avgTime = (monthTotalTime/dateList.size).toInt()
        val maxTime = (studyTimeList.max()).toInt()
        binding.monthlyTotalTime.text = "${monthTotalTimeInt/60}시간 ${monthTotalTimeInt%60}분"
        binding.AvgTime.text = "${avgTime/60}시간 ${avgTime%60}분"
        binding.MaxTime.text = "${maxTime/60}시간 ${maxTime%60}분"

        initBarChart(binding.studyMonthlyTime)
        setupChart(binding.studyMonthlyTime,dateList,studyTimeList)
        
        return binding.root
    }
    private fun setupChart(barChart: BarChart, dateList: List<String>, studyTimeList: MutableList<Double>){

        //줌인, 줌아웃 설정
        barChart.setScaleEnabled(false)

        val valueList = ArrayList<BarEntry>()  //데이터
        val title = "월별 공부량" //차트 이름

        //데이터 삽입
        for(i in dateList.indices){
            val entry = BarEntry(i.toFloat() + 1f, studyTimeList[i].toFloat())
            valueList.add(entry)
        }

        val barDataSet = BarDataSet(valueList, title)
        barDataSet.color = Color.parseColor("#32D2CA")

        val data = BarData(barDataSet)
        barChart.data = data

        // Y축에 시간 포맷터 적용
        barChart.axisLeft.valueFormatter = StudyActivity.TimeValueFormatter()
        barChart.axisRight.valueFormatter = StudyActivity.TimeValueFormatter()

        barChart.invalidate()
    }

    private fun initBarChart(barChart: BarChart) {

        val description = Description()
        // 오른쪽 하단 모서리 설명 레이블 텍스트 표시 (default = false)
        description.isEnabled = false
        barChart.description = description

        // X, Y 바의 애니메이션 효과
        barChart.animateY(1000)
        barChart.animateX(1000)

        // 바텀 좌표 값
        val xAxis: XAxis = barChart.xAxis
        // x축 위치 설정
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        // 그리드 선 수평 거리 설정
        xAxis.granularity = 1f
        // x축 텍스트 컬러 설정
        xAxis.textColor = Color.BLACK
        // x축 선 설정 (default = true)
        xAxis.setDrawAxisLine(false)
        // 격자선 설정 (default = true)
        xAxis.setDrawGridLines(false)

        val leftAxis: YAxis = barChart.axisLeft
        // 좌측 선 설정 (default = true)
        leftAxis.setDrawAxisLine(false)
        // 좌측 텍스트 컬러 설정
        leftAxis.textColor = Color.BLACK

        // 바차트의 타이틀
        val legend: Legend = barChart.legend
        // 타이틀 텍스트 사이즈 설정
        legend.textSize = 20f
        // 타이틀 텍스트 컬러 설정
        legend.textColor = Color.BLACK
        // 범례 위치 설정
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        // 범례 방향 설정
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        // 차트 내부 범례 위치하게 함 (default = false)
        legend.setDrawInside(true)
    }

}