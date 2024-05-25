package com.example.mobileteamproject

import android.content.Context
import android.os.Bundle
import android.os.SystemClock.elapsedRealtime
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileteamproject.databinding.ActivityStudyStopWatchBinding
import java.io.File
import kotlin.concurrent.timer
import kotlin.math.abs

class StudyStopWatch : AppCompatActivity() {
    lateinit var binding: ActivityStudyStopWatchBinding
//    lateinit var toggle: ActionBarDrawerToggle
    var initTime = 0L
    var pauseTime = 0L
    var totalTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudyStopWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "공부"

        //오늘 날짜
        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val date = dateFormat.format(java.util.Date())
        var todayStudyTime = 0.0
        val tmpdb = openOrCreateDatabase("studydb",Context.MODE_PRIVATE, null)
        //오늘 데이터 추출

        val nowCursor = tmpdb.rawQuery("Select STUDYTIME FROM STUDY_TB WHERE DATE = ?", arrayOf(date))
       //이미 데이터가 있으면 추출
        if (nowCursor.moveToFirst()){
            todayStudyTime = nowCursor.getDouble(0)
        }
        nowCursor.close()
        tmpdb.close()

        var timerStudyTime = (todayStudyTime *60 *1000).toLong() //밀리초 변환

        totalTime = timerStudyTime * -1
        //원래 있던 시간 그대로 반양
        binding.resultTime.base = elapsedRealtime() - timerStudyTime

        //start버튼
        binding.startBtn.setOnClickListener {
            binding.stopwatch.base = elapsedRealtime() + pauseTime  //다시 시작하는 시간
            binding.stopwatch.start()
            binding.startBtn.isEnabled = false
            binding.stopBtn.isEnabled = true
            binding.resetBtn.isEnabled = true
        }
        //stop버튼
        binding.stopBtn.setOnClickListener {
            pauseTime = binding.stopwatch.base - elapsedRealtime()
            Log.d("aaa","$pauseTime")
            binding.stopwatch.stop()
            binding.startBtn.isEnabled = true
            binding.stopBtn.isEnabled = false
            binding.resetBtn.isEnabled = true
        }
        //reset버튼
        binding.resetBtn.setOnClickListener {
            Log.d("Yang", "${binding.resultTime.base}")
            //stop버튼을 누르고 있는 경우
            if(binding.startBtn.isEnabled){
                totalTime += pauseTime
                binding.resultTime.base = elapsedRealtime() + totalTime
            }
            //start버튼을 누르고 있는 경우
            else {
                pauseTime = binding.stopwatch.base - elapsedRealtime()  //reset버튼 시 시간
                totalTime += pauseTime
                binding.resultTime.base = elapsedRealtime() + totalTime
           }
            pauseTime = 0L
            binding.stopwatch.base = elapsedRealtime()
            binding.stopwatch.stop()
            binding.startBtn.isEnabled = true
            binding.stopBtn.isEnabled = false
            binding.resetBtn.isEnabled = false

            val db = openOrCreateDatabase("studydb",Context.MODE_PRIVATE, null)
            val tmpTime = abs(totalTime)
            // totalTime을 시, 분, 초로 변환
            val hours = tmpTime / (1000 * 60 * 60) % 24
            val minutes = tmpTime / (1000 * 60) % 60
            val seconds = (tmpTime / 1000) % 60
            // 분
            val realTime =  hours*60.0+ minutes+ seconds/60.0

            val cursor = db.rawQuery("Select COUNT(*) FROM STUDY_TB WHERE DATE = ?", arrayOf(date))
            cursor.moveToFirst()
            //이미 오늘 등록했으면
            if (cursor.getInt(0)>0)
                db.execSQL("UPDATE STUDY_TB SET STUDYTIME = ? WHERE DATE = ?", arrayOf(realTime,date))
            else    //없으면
                db.execSQL("INSERT INTO STUDY_TB (DATE,STUDYTIME) VALUES (?,?)", arrayOf(date,realTime))

            cursor.close()
            db.close()
        }
    }
}