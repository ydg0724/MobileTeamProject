package com.example.mobileteamproject

import android.app.TimePickerDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.*

class TimerTabTimerFragment : Fragment() {

    lateinit var timerTextView: TextView
    lateinit var startButton: Button
    lateinit var stopButton: Button
    lateinit var resetButton: Button
    lateinit var ThirtySecondsButton: Button
    lateinit var oneMinuteButton: Button
    lateinit var oneMinuteThirtySecondsButton: Button

    var timer: CountDownTimer? = null
    var timeLeftInMillis: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer_tab_timer, container, false)

        timerTextView = view.findViewById(R.id.timerTextView)
        startButton = view.findViewById(R.id.startButton)
        stopButton = view.findViewById(R.id.stopButton)
        resetButton = view.findViewById(R.id.resetButton)
        ThirtySecondsButton = view.findViewById(R.id.ThirtySecondsButton)
        oneMinuteButton = view.findViewById(R.id.oneMinuteButton)
        oneMinuteThirtySecondsButton = view.findViewById(R.id.oneThirtySecondsButton)

        timerTextView.setOnClickListener { showTimePickerDialog() }
        startButton.setOnClickListener { startTimer() }
        stopButton.setOnClickListener { stopTimer() }
        resetButton.setOnClickListener { resetTimer() }
        ThirtySecondsButton.setOnClickListener{setTimer(1*30)}
        oneMinuteButton.setOnClickListener { setTimer(1 * 60) } // 1분
        oneMinuteThirtySecondsButton.setOnClickListener { setTimer(1 * 60 + 30) } // 1분 30초

        return view
    }

    fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                // 타이머 종료 시 동작
            }
        }.start()

        startButton.isEnabled = false
        stopButton.isEnabled = true
    }

    fun stopTimer() {
        timer?.cancel()
        startButton.isEnabled = true
        stopButton.isEnabled = false
    }

    fun resetTimer() {
        timer?.cancel()
        timeLeftInMillis = 0
        updateCountDownText()
        startButton.isEnabled = true
        stopButton.isEnabled = false
    }

    fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val seconds = 0 // 초를 0으로 설정하거나 사용자가 선택할 수 있도록 설정 가능
                val totalSeconds = (hourOfDay * 3600) + (minute * 60) + seconds
                setTimer(totalSeconds)
            },
            currentHour,
            currentMinute,
            true
        )
        timePickerDialog.show()
    }

    fun setTimer(totalSeconds: Int) {
        timeLeftInMillis = totalSeconds * 1000L
        updateCountDownText()
    }
    fun updateCountDownText() {
        val hours = (timeLeftInMillis / 1000) / 3600
        val minutes = ((timeLeftInMillis / 1000) % 3600) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
        timerTextView.text = timeLeftFormatted
    }
}
