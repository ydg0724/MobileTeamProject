package com.example.mobileteamproject

import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.ListView
import androidx.fragment.app.Fragment

class TimerTabStopwatchFragment : Fragment() {

    lateinit var chronometer: Chronometer
    lateinit var startButton: Button
    lateinit var stopButton: Button
    lateinit var recordButton: Button
    lateinit var resetButton: Button
    lateinit var listView: ListView

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
        recordButton = view.findViewById(R.id.recordButton)
        resetButton = view.findViewById(R.id.resetButton)
        listView = view.findViewById(R.id.listView)

        startButton.setOnClickListener { startChronometer() }
        stopButton.setOnClickListener { stopChronometer() }
        recordButton.setOnClickListener { recordLap() }
        resetButton.setOnClickListener { resetChronometer() }

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
        listView.adapter = null
    }

    fun recordLap() {
        val timeElapsed = chronometer.text.toString()
        val lapTime = "Lap $lapIndex: $timeElapsed"

        val adapter = listView.adapter as? RecordAdapter ?: RecordAdapter(requireContext(), mutableListOf())
        adapter.addRecord(lapTime)
        listView.adapter = adapter
        lapIndex++
    }

    val runnable = object : Runnable {
        override fun run() {
            handler.postDelayed(this, 1000)
            if (running) {
                // 시간이 지날 때마다 수행할 작업
            }
        }
    }
}
