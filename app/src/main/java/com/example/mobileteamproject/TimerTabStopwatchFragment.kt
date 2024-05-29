package com.example.mobileteamproject

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.EditText
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class TimerTabStopwatchFragment : Fragment() {

    lateinit var chronometer: Chronometer
    lateinit var startButton: Button
    lateinit var stopButton: Button
    lateinit var resetButton: Button
    lateinit var spinnerActivity: Spinner
    lateinit var etWeight: EditText
    lateinit var etTime: EditText
    lateinit var btnCalculate: Button
    lateinit var tvResult: TextView

    var running = false
    var pauseOffset: Long = 0
    var handler = Handler()
    var lapIndex = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer_tab_stopwatch, container, false)

        chronometer = view.findViewById(R.id.chronometer)
        startButton = view.findViewById(R.id.startButton)
        stopButton = view.findViewById(R.id.stopButton)
        resetButton = view.findViewById(R.id.resetButton)

        startButton.setOnClickListener { startChronometer() }
        stopButton.setOnClickListener { stopChronometer() }
        resetButton.setOnClickListener { resetChronometer() }

        spinnerActivity = view.findViewById(R.id.spinnerActivity)
        etWeight = view.findViewById(R.id.etWeight)
        etTime = view.findViewById(R.id.etTime)
        btnCalculate = view.findViewById(R.id.btnCalculate)
        tvResult = view.findViewById(R.id.tvResult)

        val activities = arrayOf("달리기", "자전거", "걷기")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, activities)
        spinnerActivity.adapter = adapter

        btnCalculate.setOnClickListener {
            calculateCalories()
        }

        return view
    }

    fun startChronometer() {
        if (!running) {
            chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
            chronometer.start()
            running = true
            startButton.isEnabled = false
            stopButton.isEnabled = true
            handler.postDelayed(runnable, 0)
        }
    }

    fun stopChronometer() {
        if (running) {
            chronometer.stop()
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
            running = false
            startButton.isEnabled = true
            stopButton.isEnabled = false
        }
    }

    fun resetChronometer() {
        chronometer.base = SystemClock.elapsedRealtime()
        pauseOffset = 0
        lapIndex = 1
        running = false
        startButton.isEnabled = true
        stopButton.isEnabled = false
    }


    val runnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 1000)
            if (running) {
                // 시간이 지날 때마다 수행할 작업
            }
        }
    }
    private fun calculateCalories() {
        val weightString = etWeight.text.toString()
        val timeString = etTime.text.toString()

        if (weightString.isEmpty() || timeString.isEmpty()) {
            Toast.makeText(requireContext(), "모든 필드를 입력하세요", Toast.LENGTH_SHORT).show()
            return
        }

        val weight = weightString.toDouble()
        val time = timeString.toInt()
        val activity = spinnerActivity.selectedItem.toString()

        val caloriesBurned = when (activity) {
            "달리기" -> calculateCaloriesRunning(weight, time)
            "자전거" -> calculateCaloriesCycling(weight, time)
            "걷기" -> calculateCaloriesWalking(weight, time)
            else -> 0.0 // 기타 활동일 경우 0.0 반환
        }

        tvResult.text = "소모된 칼로리: $caloriesBurned kcal"
    }
    private fun calculateCaloriesRunning(weight: Double, time: Int): Int {
        val MET = 8.0 // 달리기의 대사적 동등량
        val caloriesPerKgPerHour = MET * weight / 4 // 시간당 체중당 소모 칼로리
        val hours = time.toDouble() / 60 // 시간을 분 단위에서 시간 단위로 변환
        return calculateCalories(caloriesPerKgPerHour, hours)
    }

    private fun calculateCaloriesCycling(weight: Double, time: Int): Int {
        val MET = 6.0 // 자전거의 대사적 동등량
        val caloriesPerKgPerHour = MET * weight /2 // 시간당 체중당 소모 칼로리
        val hours = time.toDouble() / 60 // 시간을 분 단위에서 시간 단위로 변환
        return calculateCalories(caloriesPerKgPerHour, hours)
    }

    private fun calculateCaloriesWalking(weight: Double, time: Int): Int {
        val MET = 3.0 // 걷기의 대사적 동등량
        val caloriesPerKgPerHour = MET * weight/3 // 시간당 체중당 소모 칼로리
        val hours = time.toDouble() / 60 // 시간을 분 단위에서 시간 단위로 변환
        return calculateCalories(caloriesPerKgPerHour, hours)
    }
    fun calculateCalories(caloriesPerKgPerHour: Double, hours: Double): Int {
        val result = (caloriesPerKgPerHour * hours).toInt()
        return result
    }
}
