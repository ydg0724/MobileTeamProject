package com.example.mobileteamproject

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.os.Bundle
import android.service.carrier.CarrierMessagingService.ResultCallback
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileteamproject.databinding.ActivityMainBinding
import com.example.mobileteamproject.databinding.TodoMainBinding
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    val todoDatas = mutableListOf<String>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Every Note"

        //database 생성 혹은 읽기
        val path: File = getDatabasePath("tododb")
        if (path.exists().not()) {
            val db = openOrCreateDatabase("tododb", Context.MODE_PRIVATE, null)
            db.execSQL("create table TODO_TB (_id integer primary key autoincrement, data text not null)")
            db.execSQL(" insert into TODO_TB (data) values ('모바일 팀플 과제하기')")
            db.execSQL(" insert into TODO_TB (data) values ('운영체제 공부하기')")
            db.execSQL(" insert into TODO_TB (data) values ('네트워크 프로그래밍 과제하기')")
            db.close()
        }

        //리사이클러뷰 어댑터 바인딩
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ResultAdapter(todoDatas)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this,
            LinearLayoutManager.VERTICAL))
        adapter.listItemClickListenerFunc(object: SetOnClickListenerInterface {
            override fun listItemClickListener(itemData: String, binding: TodoMainBinding) {
                val db = openOrCreateDatabase("tododb", Context.MODE_PRIVATE, null)
                db.execSQL("delete from TODO_TB where data = ?",
                    arrayOf(itemData))
                val cursor = db.rawQuery("select data from TODO_TB", null)
                todoDatas.clear()
                while (cursor.moveToNext()) {
                    todoDatas.add(cursor.getString(0))
                }
                db.close()
                adapter.notifyDataSetChanged()
            }
        })

        //db 열고 데이터 읽기
        val db = openOrCreateDatabase("tododb", Context.MODE_PRIVATE, null)
        val cursor = db.rawQuery("select data from TODO_TB", null)
        todoDatas.clear()
        while (cursor.moveToNext()) {
            todoDatas.add(cursor.getString(0))
        }
        db.close()

        //왼쪽 위 툴 메뉴바 바인딩, 이벤트
        toggle = ActionBarDrawerToggle(this, binding.drawer,
            R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        binding.menu1.setOnClickListener {
            Toast.makeText(this, "현재 속한 페이지", Toast.LENGTH_SHORT).show()
            binding.drawer.close()
        }
        binding.menu2.setOnClickListener {
            startActivity(Intent(this, PhraseActivity::class.java))
            binding.drawer.close()
        }
        binding.menu3.setOnClickListener {
            startActivity(Intent(this, StudyActivity::class.java))
            binding.drawer.close()
        }
        binding.menu4.setOnClickListener {
            startActivity(Intent(this, SportActivity::class.java))
            binding.drawer.close()
        }
        binding.menu5.setOnClickListener {
            startActivity(Intent(this, ReadActivity::class.java))
            binding.drawer.close()
        }

        binding.addBtn.shrink()
        binding.addBtn.setOnClickListener {
            when (binding.addBtn.isExtended) {
                true -> {
                    binding.addBtn.shrink()
                    startActivity(Intent(this, AddActivity::class.java))
                }
                false -> binding.addBtn.extend()
            }
        }

        //study 데이터 생성
        val studypath: File = getDatabasePath("studydb")
        if (studypath.exists().not()) {
            val studydb = openOrCreateDatabase("studydb", MODE_PRIVATE, null)
            studydb.execSQL(
                "create table STUDY_TB(_id integer primary key autoincrement," +
                        " DATE text not null, STUDYTIME float not null )"
            )

            studydb.execSQL("delete from STUDY_TB")  //원래 생성되어있던 데이터 삭제

            val startDate = LocalDate.of(2024, 1, 1)
            val endDate = LocalDate.now()
            val dateList = mutableListOf<String>()
            val studyTimeList = mutableListOf<Double>()


            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            var currentDate = startDate
            Log.d("yang","dateFormatBTN : $endDate")
            while (!currentDate.isAfter(endDate)) {
                // 날짜 추가
                dateList.add(currentDate.format(dateFormatter))

                // 임의의 공부 시간 생성 (예: 0~5시간, 0~59분, 0~59초)
                val hours = Random.nextInt(1, 5)
                val minutes = Random.nextInt(0, 60)
                val seconds = Random.nextInt(0, 60)
                studyTimeList.add(hours * 60.0 + minutes + (seconds/60.0))

                // 다음 날짜로
                currentDate = currentDate.plusDays(1)
            }
            for(i in dateList.indices) {
                // ContentValues를 사용하여 데이터 추가
                val values = ContentValues().apply {
                    put("DATE", dateList[i])
                    put("STUDYTIME", studyTimeList[i])
                }

                // 데이터베이스에 데이터 삽입
                studydb.insert("STUDY_TB", null, values)

            }
            studydb.close()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

//리사이클러뷰 어댑터
class ResultHolder(val binding: TodoMainBinding): RecyclerView.ViewHolder(binding.root)

class ResultAdapter(val todoDatas: MutableList<String>):
    RecyclerView.Adapter<ResultHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder =
        ResultHolder(TodoMainBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    var onClickListener: SetOnClickListenerInterface? = null
    fun listItemClickListenerFunc(pOnClick: SetOnClickListenerInterface) {
        this.onClickListener = pOnClick
    }
    override fun getItemCount(): Int = todoDatas.size
    override fun onBindViewHolder(holder: ResultHolder, position: Int) {
        holder.binding.todoData.text = todoDatas[position]
        holder.binding.todoDelete.setOnClickListener {
            onClickListener?.listItemClickListener(todoDatas[position], holder.binding)
        }
    }
}

interface SetOnClickListenerInterface {
    fun listItemClickListener(itemData: String, binding: TodoMainBinding)
}



