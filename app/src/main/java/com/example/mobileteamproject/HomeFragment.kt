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
        val memoText1 = "모바일 프로그래밍 발표"
        val date1 = "2024-05-30"
        val memoText2 = "모바일 프로그래밍 시험"
        val date2 = "2024-06-14"
        val dbHelper = MemoDatabaseHelper(requireContext())
        dbHelper.addOrUpdateMemo(date2, memoText2)
        dbHelper.addOrUpdateMemo(date1, memoText1)

        loadMemos()
    }

    private fun loadMemos() {
        val dbHelper = MemoDatabaseHelper(requireContext())
        val memos = dbHelper.getAllMemos()
        memoAdapter = MemoAdapter(memos)
        recyclerView.adapter = memoAdapter
    }
}
