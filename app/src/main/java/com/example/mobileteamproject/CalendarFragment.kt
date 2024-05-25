package com.example.mobileteamproject

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var diaryTextView: TextView
    private lateinit var contextEditText: EditText
    private lateinit var saveBtn: Button
    private lateinit var updateBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var diaryContent: TextView

    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // View 초기화
        calendarView = view.findViewById(R.id.calendarView)
        diaryTextView = view.findViewById(R.id.diaryTextView)
        contextEditText = view.findViewById(R.id.contextEditText)
        saveBtn = view.findViewById(R.id.saveBtn)
        updateBtn = view.findViewById(R.id.updateBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)
        diaryContent = view.findViewById(R.id.diaryContent)

        // CalendarView 날짜 변경 리스너 설정
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
            diaryTextView.text = selectedDate
            loadDiary()
        }

        saveBtn.setOnClickListener {
            saveDiary()
            contextEditText.text = null
        }

        updateBtn.setOnClickListener {
            updateDiary()
        }

        deleteBtn.setOnClickListener {
            deleteDiary()
        }
    }

    private fun loadDiary() {
        val dbHelper = MemoDatabaseHelper(requireContext())
        val diaryText = dbHelper.getMemo(selectedDate)
        if (diaryText != null) {
            contextEditText.visibility = View.INVISIBLE
            diaryContent.visibility = View.VISIBLE
            diaryContent.text = diaryText
            saveBtn.visibility = View.INVISIBLE
            updateBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE
        } else {
            contextEditText.visibility = View.VISIBLE
            diaryContent.visibility = View.INVISIBLE
            saveBtn.visibility = View.VISIBLE
            updateBtn.visibility = View.INVISIBLE
            deleteBtn.visibility = View.INVISIBLE
        }
    }

    private fun saveDiary() {
        val text = contextEditText.text.toString()
        if (text.isNotEmpty()) {
            val dbHelper = MemoDatabaseHelper(requireContext())
            dbHelper.addOrUpdateMemo(selectedDate, text)
            loadDiary()
        } else {
            Toast.makeText(activity, "내용을 입력하세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateDiary() {
        contextEditText.visibility = View.VISIBLE
        diaryContent.visibility = View.INVISIBLE
        saveBtn.visibility = View.VISIBLE
        updateBtn.visibility = View.INVISIBLE
        deleteBtn.visibility = View.INVISIBLE
        contextEditText.setText(diaryContent.text)
    }

    private fun deleteDiary() {
        val dbHelper = MemoDatabaseHelper(requireContext())
        dbHelper.deleteMemo(selectedDate)
        contextEditText.text.clear()
        loadDiary()
    }
}
