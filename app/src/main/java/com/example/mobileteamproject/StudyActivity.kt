package com.example.mobileteamproject

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileteamproject.databinding.ActivityStudyBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class StudyActivity : AppCompatActivity() {
    lateinit var binding: ActivityStudyBinding
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "공부"

        toggle = ActionBarDrawerToggle(this, binding.drawer,    //drawer 레이아웃
            R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        /*메뉴 이동*/
        binding.menu1.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            binding.drawer.close()
        }
        binding.menu2.setOnClickListener {
            startActivity(Intent(this, PhraseActivity::class.java))
            binding.drawer.close()
        }
        binding.menu3.setOnClickListener {
            Toast.makeText(this, "현재 속한 페이지", Toast.LENGTH_SHORT).show()
            binding.drawer.close()
        }
        binding.menu4.setOnClickListener {
            startActivity(Intent(this, SportActivity::class.java))
            binding.drawer.close()
        }
        binding.menu5.setOnClickListener {
            startActivity(Intent(this, ReadActivity::class.java))
            binding.drawer.close()
        }
        /*하단 버튼 이동*/
        binding.goToTodo.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            binding.drawer.close()
        }
        binding.gotoPhrase.setOnClickListener {
            startActivity(Intent(this, PhraseActivity::class.java))
            binding.drawer.close()
        }
        binding.goToStopwatch.setOnClickListener {
            startActivity(Intent(this, StudyStopWatch::class.java))
            binding.drawer.close()
        }
        binding.gotoPhrase.setOnClickListener {
            startActivity(Intent(this, ReadActivity::class.java))
            binding.drawer.close()
        }
        initBarChart(binding.studyWeekTime) //그래프 기본설정
        setupChart(binding.studyWeekTime)   //그래프 데이터세팅
    }
    //막대 그래프
    private fun setupChart(barChart: BarChart){
        
        //줌인, 줌아웃 설정
        barChart.setScaleEnabled(false)
        
        val valueList = ArrayList<BarEntry>()  //데이터
        val title = "주 공부량" //차트 이름

        //데이터 삽입
        valueList.add(BarEntry(1.0f,20.0f))
        valueList.add(BarEntry(2.0f,20.0f))
        valueList.add(BarEntry(3.0f,30.0f))

        val barDataSet = BarDataSet(valueList, title)

        barDataSet.setColors(
            Color.BLUE
        )

        val data = BarData(barDataSet)
        barChart.data = data
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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}