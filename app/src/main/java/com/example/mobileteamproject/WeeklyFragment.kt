package com.example.mobileteamproject

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobileteamproject.databinding.FragmentWeeklyBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry


class WeeklyFragment : Fragment() {
    lateinit var binding : FragmentWeeklyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeeklyBinding.inflate(inflater, container, false)

        val db = requireContext().openOrCreateDatabase("studydb", MODE_PRIVATE, null)

        initBarChart(binding.studyWeeklyTime)
        return binding.root
    }


    private fun setupChart(barChart: BarChart, dateList: List<String>, studyTimeList: List<String>){

        //줌인, 줌아웃 설정
        barChart.setScaleEnabled(false)

        val valueList = ArrayList<BarEntry>()  //데이터
        val title = "최근 1주일 공부량" //차트 이름

        //데이터 삽입
        for(i in dateList.indices){
            //시:분:초를
            val parts = studyTimeList[i].split(":")
            val hours = parts[0].toInt()
            val minutes = parts[1].toInt()
            val seconds = parts[2].toInt()
            val lastStudyTime = hours * 60f + minutes + seconds / 60f
            val entry = BarEntry(i.toFloat() + 1f, lastStudyTime)
            valueList.add(entry)
        }

        val barDataSet = BarDataSet(valueList, title)
        barDataSet.setColors(Color.BLUE)

        val data = BarData(barDataSet)
        barChart.data = data

        // X축에 날짜 포맷터 적용
        barChart.xAxis.valueFormatter = StudyActivity.DateValueFormatter(dateList)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM // X축을 아래로 설정
        barChart.xAxis.granularity = 1.2f // X축 간격

        // Y축에 시간 포맷터 적용
        /*barChart.axisLeft.valueFormatter = StudyActivity.TimeValueFormatter()
        barChart.axisRight.valueFormatter = StudyActivity.TimeValueFormatter() // 오른쪽 Y축 비활성화*/
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