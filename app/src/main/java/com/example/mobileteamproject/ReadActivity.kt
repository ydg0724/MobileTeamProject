package com.example.mobileteamproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobileteamproject.databinding.ActivityReadBinding
import com.example.mobileteamproject.databinding.DialogPageBinding
import com.example.mobileteamproject.databinding.ListReadBinding
import com.example.mobileteamproject.databinding.PageReadBinding
import com.example.mobileteamproject.databinding.TodoMainBinding
import java.io.File

class ReadActivity : AppCompatActivity() {
    lateinit var binding: ActivityReadBinding
    lateinit var toggle: ActionBarDrawerToggle
    val titles = mutableListOf<String>()
    val pages = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "책갈피"

        //database 생성 혹은 읽기
        val path: File = getDatabasePath("readdb")
        if (path.exists().not()) {
            val db = openOrCreateDatabase("readdb", Context.MODE_PRIVATE, null)
            db.execSQL("create table PAGE_TB (_id integer primary key autoincrement, title text not null, page text)")
            db.execSQL("create table BOOK_TB (_id integer primary key autoincrement, title text not null, rate float, note text)")
            db.execSQL(" insert into PAGE_TB (title, page) values ('어린왕자', '145')")
            db.execSQL(" insert into PAGE_TB (title, page) values ('호밀밭의 파수꾼', '213')")
            db.execSQL(" insert into PAGE_TB (title, page) values ('동물농장', '73')")
            db.execSQL(" insert into BOOK_TB (title, rate, note) values ('어린왕자', 4.5, '너의 장미꽃이 그토록 소중한 것은 그 꽃을 위해 네가 공들인 그 시간 때문이야" +
                    "\n\n사막이 아름다운 것은 그것이 어딘가에 오아시스를 감추고 있기 때문이야" +
                    "\n\n내가 좋아하는 사람이 나를 좋아해 주는 건 기적이야')")
            db.execSQL(" insert into BOOK_TB (title, rate, note) values ('햄릿', 3.0, '덴마크 왕이 갑자기 죽은 후 왕의 동생 클로디어스가 왕위에 오르고, " +
                    "얼마 지나지 않아 클로디어스가 선왕의 왕비 거트루드와 재혼한다. 갑작스런 아버지의 죽음과 아버지의 동생과 재혼을 한 어머니에 대한 원망에 " +
                    "사로잡힌 햄릿은 한밤중 초소에서 선왕(아버지)의 망령을 만나게 되고, 동생에 의해 독살되었다는 선왕의 말에 미친 척하며 복수를 꾀하게 된다.')")
            db.close()
        }

        //리사이클러뷰 어댑터 바인딩
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ResultAdapterRead(titles, pages)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        adapter.listItemClickListenerFunc(object: SetOnClickListenerInterfaceRead {
            override fun listItemClickListener(itemData: String, binding: PageReadBinding) {
                val db = openOrCreateDatabase("readdb", Context.MODE_PRIVATE, null)
                db.execSQL("delete from PAGE_TB where title = ?",
                    arrayOf(itemData))
                val cursor = db.rawQuery("select title, page from PAGE_TB", null)
                titles.clear()
                pages.clear()
                while (cursor.moveToNext()) {
                    titles.add(cursor.getString(0))
                    pages.add(cursor.getString(1))
                }
                db.close()
                adapter.notifyDataSetChanged()
            }
        })
        val mContext = this
        adapter.listItemClickListenerPageFunc(object: SetOnClickListenerInterfacePage {
            override fun listItemClickListener(itemData: String, binding: PageReadBinding) {
                val dialogBinding = DialogPageBinding.inflate(layoutInflater)
                AlertDialog.Builder(mContext).run {
                    setTitle("페이지 수정")
                    setView(dialogBinding.root)
                    dialogBinding.textview.text = itemData
                    setPositiveButton("확인") {_, _ ->
                        var page = dialogBinding.editview.text.toString()
                        val db = openOrCreateDatabase("readdb", Context.MODE_PRIVATE, null)
                        db.execSQL("update PAGE_TB set page = '${page}' where title = ?",
                            arrayOf(itemData))
                        val cursor = db.rawQuery("select title, page from PAGE_TB", null)
                        titles.clear()
                        pages.clear()
                        while (cursor.moveToNext()) {
                            titles.add(cursor.getString(0))
                            pages.add(cursor.getString(1))
                        }
                        db.close()
                        adapter.notifyDataSetChanged()
                    }
                    setNegativeButton("취소", null)
                    show()
                }
            }
        })

        //db 열고 데이터 읽기
        val db = openOrCreateDatabase("readdb", Context.MODE_PRIVATE, null)
        val cursor = db.rawQuery("select title, page from PAGE_TB", null)
        titles.clear()
        pages.clear()
        while (cursor.moveToNext()) {
            titles.add(cursor.getString(0))
            pages.add(cursor.getString(1))
        }
        db.close()

        binding.addBtn.setOnClickListener {
            val db = openOrCreateDatabase("readdb", Context.MODE_PRIVATE, null)
            db.execSQL("insert into PAGE_TB(title, page) values(?,?)",
                arrayOf(binding.editTitle.text.toString(), binding.editPage.text.toString())
            )
            val cursor = db.rawQuery("select title, page from PAGE_TB", null)
            titles.clear()
            pages.clear()
            while (cursor.moveToNext()) {
                titles.add(cursor.getString(0))
                pages.add(cursor.getString(1))
            }
            db.close()
            adapter.notifyDataSetChanged()
            binding.editTitle.text = null
            binding.editPage.text = null
        }

        //왼쪽 위 툴 메뉴바 바인딩, 이벤트
        toggle = ActionBarDrawerToggle(this, binding.drawer,
            R.string.drawer_opened, R.string.drawer_closed)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        binding.menu1.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
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
            Toast.makeText(this, "현재 속한 페이지", Toast.LENGTH_SHORT).show()
            binding.drawer.close()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_read, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        when (item.itemId) {
            R.id.bookList -> startActivity(Intent(this, BookActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}

//리사이클러뷰 어댑터
class ResultHolderRead(val binding: PageReadBinding): RecyclerView.ViewHolder(binding.root)

class ResultAdapterRead(val titles: MutableList<String>, val pages: MutableList<String>):
    RecyclerView.Adapter<ResultHolderRead>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolderRead =
        ResultHolderRead(PageReadBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    var onClickListener: SetOnClickListenerInterfaceRead? = null
    var onPageClickListener: SetOnClickListenerInterfacePage? = null
    fun listItemClickListenerFunc(pOnClick: SetOnClickListenerInterfaceRead) {
        this.onClickListener = pOnClick
    }
    fun listItemClickListenerPageFunc(pOnClick: SetOnClickListenerInterfacePage) {
        this.onPageClickListener = pOnClick
    }
    override fun getItemCount(): Int = titles.size
    override fun onBindViewHolder(holder: ResultHolderRead, position: Int) {
        holder.binding.textview.text = titles[position] + " - " + pages[position]
        holder.binding.deleteBtn.setOnClickListener {
            onClickListener?.listItemClickListener(titles[position], holder.binding)
        }
        holder.binding.textview.setOnClickListener {
            onPageClickListener?.listItemClickListener(titles[position], holder.binding)
        }
    }
}
interface SetOnClickListenerInterfaceRead {
    fun listItemClickListener(itemData: String, binding: PageReadBinding)
}

interface SetOnClickListenerInterfacePage {
    fun listItemClickListener(itemData: String, binding: PageReadBinding)
}