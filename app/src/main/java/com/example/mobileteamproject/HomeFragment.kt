package com.example.mobileteamproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var memoAdapter: MemoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val memoText1 = "등 + 이두 운동"
        val date1 = "2024-05-30"
        val memoText2 = "가슴 + 삼두 운동"
        val date2 = "2024-05-31"
        val memoText3 = "하체 + 어깨 운동"
        val date3 = "2024-06-01"
        val memoText4 = "3대 측정"
        val date4 = "2024-06-14"
        val dbHelper = MemoDatabaseHelper(requireContext())
        dbHelper.addOrUpdateMemo(date4, memoText4)
        dbHelper.addOrUpdateMemo(date3, memoText3)
        dbHelper.addOrUpdateMemo(date2, memoText2)
        dbHelper.addOrUpdateMemo(date1, memoText1)

        loadMemos()
    }

    private fun loadMemos() {
        val dbHelper = MemoDatabaseHelper(requireContext())
        val today = dbHelper.getTodayDate()
        val oneMonthAgo = dbHelper.getDateStringFromToday(-30)
        val oneMonthLater = dbHelper.getDateStringFromToday(30)
        val memos = dbHelper.getMemosInRange(oneMonthAgo, oneMonthLater)
        memoAdapter = MemoAdapter(memos)
        recyclerView.adapter = memoAdapter
    }
}