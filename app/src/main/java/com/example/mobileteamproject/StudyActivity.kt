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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
        /*하단 버튼 이동*/
        /*binding.goToTodo.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            binding.drawer.close()
        }*/
        binding.goToStatistics.setOnClickListener {
            startActivity(Intent(this, StatisticsActivity::class.java))
            binding.drawer.close()
        }
        binding.goToStopwatch.setOnClickListener {
            startActivity(Intent(this, StudyStopWatch::class.java))
            binding.drawer.close()
        }
        /*binding.gotoPhrase.setOnClickListener {
            startActivity(Intent(this, PhraseActivity::class.java))
            binding.drawer.close()
        }*/

        //db 테이블 생성
        val path: File = getDatabasePath("studydb")
        if(path.exists().not()) {
            val db = openOrCreateDatabase("studydb", MODE_PRIVATE, null)
            db.execSQL(
                "create table STUDY_TB(_id integer primary key autoincrement," +
                        " DATE text not null, STUDYTIME float not null )"
            )
            db.close()
        }

        //데이터 생성
        binding.tmpData.setOnClickListener {
            val db = openOrCreateDatabase("studydb", MODE_PRIVATE, null)

            val startDate = LocalDate.of(2024, 1, 1)
            val endDate = LocalDate.of(2024, 5, 18)
            val dateList = mutableListOf<String>()
            val studyTimeList = mutableListOf<Double>()

            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            var currentDate = startDate

            while (!currentDate.isAfter(endDate)) {
                // 날짜 추가
                dateList.add(currentDate.format(dateFormatter))

                // 임의의 공부 시간 생성 (예: 0~5시간, 0~59분, 0~59초)
                val hours = Random.nextInt(1, 5)
                val minutes = Random.nextInt(0, 60)
                val seconds = Random.nextInt(0, 60)
                studyTimeList.add(hours * 60.0 + minutes + (seconds/60.0))

                // 다음 날짜로
                currentDate = currentDate.plusDays(1)
            }
                for(i in dateList.indices) {
                    // ContentValues를 사용하여 데이터 추가
                    val values = ContentValues().apply {
                        put("DATE", dateList[i])
                        put("STUDYTIME", studyTimeList[i])
                    }

                    // 데이터베이스에 데이터 삽입
                    db.insert("STUDY_TB", null, values)

                }
            db.close()
            }
        //데이터 삭제
        binding.deleteData.setOnClickListener {
            val db = openOrCreateDatabase("studydb", MODE_PRIVATE, null)

            db.execSQL("delete from STUDY_TB")
            db.close()
        }

        val db = openOrCreateDatabase("studydb", MODE_PRIVATE, null)
        //7일간의 데이터 뽑는 쿼리문
        val query = """
            SELECT * FROM STUDY_TB
            WHERE DATE >= date('now', '-5 days')
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

        Log.d("yang","dataList : $dateList")
        Log.d("yang", "studyTimeList : $studyTimeList")

        initBarChart(binding.studyWeekTime) //그래프 기본설정
        setupChart(binding.studyWeekTime,dateList,studyTimeList)   //그래프 데이터세팅
    }
    //막대 그래프
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
        barDataSet.setColors(Color.BLUE)

        val data = BarData(barDataSet)
        barChart.data = data

        // X축에 날짜 포맷터 적용
        barChart.xAxis.valueFormatter = DateValueFormatter(dateList)
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM // X축을 아래로 설정
        barChart.xAxis.granularity = 1.2f // X축 간격

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