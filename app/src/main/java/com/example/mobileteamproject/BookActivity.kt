package com.example.mobileteamproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileteamproject.databinding.ActivityBookBinding
import com.example.mobileteamproject.databinding.ListReadBinding
import com.example.mobileteamproject.databinding.PageReadBinding
import com.example.mobileteamproject.databinding.TodoMainBinding
import java.io.File

class BookActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookBinding
    val titles = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "리스트"


        //리사이클러뷰 어댑터 바인딩
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ResultAdapterBook(titles)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

        //db 열고 데이터 읽기
        val db = openOrCreateDatabase("readdb", Context.MODE_PRIVATE, null)
        val cursor = db.rawQuery("select title from BOOK_TB", null)
        titles.clear()
        while (cursor.moveToNext()) {
            titles.add(cursor.getString(0))
        }
        db.close()
    }
}

//리사이클러뷰 어댑터
class ResultHolderBook(val binding: ListReadBinding): RecyclerView.ViewHolder(binding.root)

class ResultAdapterBook(val titles: MutableList<String>):
    RecyclerView.Adapter<ResultHolderBook>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolderBook =
        ResultHolderBook(ListReadBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    var onClickListener: SetOnClickListenerInterfaceBook? = null
    fun listItemClickListenerFunc(pOnClick: SetOnClickListenerInterfaceBook) {
        this.onClickListener = pOnClick
    }
    override fun getItemCount(): Int = titles.size
    override fun onBindViewHolder(holder: ResultHolderBook, position: Int) {
        holder.binding.title.text = titles[position]
    }
}

interface SetOnClickListenerInterfaceBook {
    fun listItemClickListener(itemData: String, binding: ListReadBinding)
}