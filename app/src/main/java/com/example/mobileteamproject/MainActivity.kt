package com.example.mobileteamproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase.openOrCreateDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileteamproject.databinding.ActivityMainBinding
import com.example.mobileteamproject.databinding.TodoMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    val todoDatas = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Title"

        val path: File = getDatabasePath("tododb")
        if (path.exists().not()) {
            val db = openOrCreateDatabase("tododb", Context.MODE_PRIVATE, null)
            db.execSQL("create table TODO_TB (_id integer primary key autoincrement, data text not null)")
            db.close()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ResultAdapter(todoDatas)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this,
            LinearLayoutManager.VERTICAL))

        val db = openOrCreateDatabase("tododb", Context.MODE_PRIVATE, null)
        val cursor = db.rawQuery("select data from TODO_TB", null)
        todoDatas.clear()
        while (cursor.moveToNext()) {
            todoDatas.add(cursor.getString(0))
        }
        db.close()
        adapter.notifyDataSetChanged()

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

        binding.addbtn.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
            binding.drawer.close()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

class ResultHolder(val binding: TodoMainBinding): RecyclerView.ViewHolder(binding.root)

class ResultAdapter(val todoDatas: MutableList<String>):
    RecyclerView.Adapter<ResultHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder =
        ResultHolder(TodoMainBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))

    override fun getItemCount(): Int = todoDatas.size + 1
    override fun onBindViewHolder(holder: ResultHolder, position: Int) {
        if (position == 0) {
            holder.binding.todoData.text = "Hello"
        } else {
            holder.binding.todoData.text = todoDatas[position]
        }
        holder.binding.todoDelete.setOnClickListener {
            val db = openOrCreateDatabase("tododb", null)
            db.execSQL("delete from TODO_TB where data = ?",
                arrayOf(holder.binding.todoData.text.toString()))
            db.close()
        }
    }
}