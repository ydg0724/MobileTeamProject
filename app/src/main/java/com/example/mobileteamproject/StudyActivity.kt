package com.example.mobileteamproject

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileteamproject.databinding.ActivityStudyBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class StudyActivity : AppCompatActivity() {
    lateinit var binding: ActivityStudyBinding
    lateinit var toggle: ActionBarDrawerToggle
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range")
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

        binding.goToStatistics.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
            binding.drawer.close()
        }
        binding.goToStopwatch.setOnClickListener {
            startActivity(Intent(this, StudyStopWatch::class.java))
            binding.drawer.close()
        }

        //db 테이블 생성
        val path: File = getDatabasePath("studydb")
        if(path.exists().not()) {
            var db = openOrCreateDatabase("studydb", MODE_PRIVATE, null)
            db.execSQL(
                "create table STUDY_TB(_id integer primary key autoincrement," +
                        " DATE text not null, STUDYTIME float not null )"
            )
            db.close()
        }



        val db = openOrCreateDatabase("studydb", MODE_PRIVATE, null)
        //7일간의 데이터 뽑는 쿼리문
        val query = """
            SELECT * FROM STUDY_TB
            WHERE DATE >= date('now', '-6 days')
            ORDER BY DATE DESC
        """.trimIndent()
        //커서 선언
        val cursor = db.rawQuery(query,null)

        val dateList = mutableListOf<String>()
        val studyTimeList = mutableListOf<Double>()
        try{
            if (cursor.moveToFirst()){
                do{
                    val date = cursor.getString(cursor.getColumnIndex("DATE"))
                    val studytime = cursor.getDouble(cursor.getColumnIndex("STUDYTIME"))
                    dateList.add(date)
                    studyTimeList.add(studytime)
                }while(cursor.moveToNext())
            }
        } finally {
            cursor.close()
            db.close()
        }


        val studydb = openOrCreateDatabase("studydb", MODE_PRIVATE, null)

        val calender = Calendar.getInstance()
        calender.add(Calendar.DATE,-1)
        val yesterdayformat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val yesterdayDate = yesterdayformat.format(calender.time)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.format(Date())


        val yesterdayCursor = studydb.rawQuery("SELECT STUDYTIME FROM STUDY_TB WHERE DATE = ?", arrayOf(yesterdayDate))
        val todayCursor = studydb.rawQuery("select STUDYTIME from STUDY_TB where DATE = ?", arrayOf(date))
        //어제 공부량 구하기
        val yesterdayTime = mutableListOf<Double>()
        while (yesterdayCursor.moveToNext())
            yesterdayTime.add(yesterdayCursor.getDouble(yesterdayCursor.getColumnIndexOrThrow("STUDYTIME")))

        //오늘의 공부량 구하기
        val studyTimes = mutableListOf<Double>()
        while (todayCursor.moveToNext())
            studyTimes.add(todayCursor.getDouble(todayCursor.getColumnIndexOrThrow("STUDYTIME")))


        if(yesterdayTime.isNotEmpty()) {
            val yesterdayMinutes = yesterdayTime[0].toInt()
            binding.yesterdayTime.text = "${yesterdayMinutes / 60}시간 ${yesterdayMinutes % 60}분"
        }else 
            binding.yesterdayTime.text = "0시간"
        
        if (studyTimes.isNotEmpty()){
            val todayMinutes = studyTimes[0].toInt()
            binding.todayTime.text = "${todayMinutes / 60}시간 ${todayMinutes % 60}분"
        }else
            binding.todayTime.text = "0시간"

        todayCursor.close()
        initBarChart(binding.studyWeekTime) //그래프 기본설정
        setupChart(binding.studyWeekTime,dateList,studyTimeList)   //그래프 데이터세팅
    }
    //막대 그래프 세팅
    private fun setupChart(barChart: BarChart, dateList: List<String>, studyTimeList: MutableList<Double>){
        
        //줌인, 줌아웃 설정
        barChart.setScaleEnabled(false)
        
        val valueList = ArrayList<BarEntry>()  //데이터
        val title = "최근 1주일 공부량" //차트 이름

        //데이터 삽입
        for(i in dateList.indices){
            val entry = BarEntry(i.toFloat() + 1f, studyTimeList[i].toFloat())
            valueList.add(entry)
        }

        val barDataSet = BarDataSet(valueList, title)
        barDataSet.color = Color.parseColor("#32D2CA")

        val data = BarData(barDataSet)
        barChart.data = data

        // X축에 날짜 포맷터 적용
        barChart.xAxis.valueFormatter = DateValueFormatter(dateList)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM // X축을 아래로 설정
        barChart.xAxis.granularity = 1f // X축 간격

        // Y축에 시간 포맷터 적용
        barChart.axisLeft.valueFormatter = TimeValueFormatter()
        barChart.axisRight.valueFormatter = TimeValueFormatter()
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

    // X축 날짜 포맷터 구현
    class DateValueFormatter(private val dateList: List<String>) : ValueFormatter() {
        @RequiresApi(Build.VERSION_CODES.O)
        val original = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        @RequiresApi(Build.VERSION_CODES.O)
        val target = DateTimeFormatter.ofPattern("MM-dd")
        @RequiresApi(Build.VERSION_CODES.O)
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt() - 1 // BarEntry에서 1을 더했으므로 여기서 1을 빼줍니다.
            if (index >= 0 && index < dateList.size) {
                // yyyy-mm-dd 형태의 날짜 문자열을 LocalDate 객체로 변환
                val date = LocalDate.parse(dateList[index], original)
                // LocalDate 객체를 mm-dd 형태의 문자열로 변환
                return date.format(target)
            }
            return ""
        }
    }
    // Y축 시간 포맷터 구현
    class TimeValueFormatter : ValueFormatter() {

        override fun getFormattedValue(value: Float): String {
            val totalMinutes = value.toInt()
            val hours = totalMinutes / 60
            val minutes = totalMinutes % 60
            val seconds = ((value - totalMinutes) * 60).toInt()
            return String.format("%d:%02d:%02d", hours, minutes, seconds)
        }
    }
}