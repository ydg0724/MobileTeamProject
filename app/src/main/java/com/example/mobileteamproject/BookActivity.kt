package com.example.mobileteamproject

import android.content.Context
import android.content.Intent
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
        adapter.listItemClickListenerFunc(object : SetOnClickListenerInterfaceBook {
            override fun listItemClickListener(itemData: String, binding: ListReadBinding) {
                val intent = Intent(applicationContext, BookNoteActivity::class.java)
                intent.putExtra("title", itemData)
                startActivity(intent)
            }
        })
        adapter.listItemClickListenerFunc(object : SetOnClickListenerInterfaceBookDelete {
            override fun listItemClickListener(itemData: String, binding: ListReadBinding) {
                val db = openOrCreateDatabase("readdb", Context.MODE_PRIVATE, null)
                db.execSQL("delete from BOOK_TB where title = ?",
                    arrayOf(itemData))
                val cursor = db.rawQuery("select title from BOOK_TB", null)
                titles.clear()
                while (cursor.moveToNext()) {
                    titles.add(cursor.getString(0))
                }
                db.close()
                adapter.notifyDataSetChanged()
            }
        })

        //db 열고 데이터 읽기
        val db = openOrCreateDatabase("readdb", Context.MODE_PRIVATE, null)
        val cursor = db.rawQuery("select title from BOOK_TB", null)
        titles.clear()
        while (cursor.moveToNext()) {
            titles.add(cursor.getString(0))
        }
        db.close()

        binding.addBtn.shrink()
        binding.addBtn.setOnClickListener {
            when (binding.addBtn.isExtended) {
                true -> {
                    binding.addBtn.shrink()
                    startActivity(Intent(this, AddBookActivity::class.java))
                }
                false -> binding.addBtn.extend()
            }
        }
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
    var onClickListenerdelete: SetOnClickListenerInterfaceBookDelete? = null
    fun listItemClickListenerFunc(pOnClick: SetOnClickListenerInterfaceBook) {
        this.onClickListener = pOnClick
    }
    fun listItemClickListenerFunc(pOnClick: SetOnClickListenerInterfaceBookDelete) {
        this.onClickListenerdelete = pOnClick
    }
    override fun getItemCount(): Int = titles.size
    override fun onBindViewHolder(holder: ResultHolderBook, position: Int) {
        holder.binding.title.text = titles[position]
        holder.binding.deleteBtn.setOnClickListener {
            onClickListenerdelete?.listItemClickListener(titles[position], holder.binding)
        }
        holder.binding.title.setOnClickListener {
            onClickListener?.listItemClickListener(titles[position], holder.binding)
        }
    }
}

interface SetOnClickListenerInterfaceBook {
    fun listItemClickListener(itemData: String, binding: ListReadBinding)
}

interface SetOnClickListenerInterfaceBookDelete {
    fun listItemClickListener(itemData: String, binding: ListReadBinding)
}