package com.example.mobileteamproject

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock.elapsedRealtime
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileteamproject.databinding.ActivityStudyStopWatchBinding

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

//        toggle = ActionBarDrawerToggle(this, binding.drawer,
//            R.string.drawer_opened, R.string.drawer_closed)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        toggle.syncState()
//
//        binding.menu1.setOnClickListener {
//            startActivity(Intent(this, MainActivity::class.java))
//            binding.drawer.close()
//        }
//        binding.menu2.setOnClickListener {
//            startActivity(Intent(this, PhraseActivity::class.java))
//            binding.drawer.close()
//        }
//        binding.menu3.setOnClickListener {
//            startActivity(Intent(this, StudyActivity::class.java))
//            binding.drawer.close()
//        }
//        binding.menu4.setOnClickListener {
//            startActivity(Intent(this, SportActivity::class.java))
//            binding.drawer.close()
//        }
//        binding.menu5.setOnClickListener {
//            startActivity(Intent(this, ReadActivity::class.java))
//            binding.drawer.close()
//        }
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
        }


    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (toggle.onOptionsItemSelected(item)){
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
}