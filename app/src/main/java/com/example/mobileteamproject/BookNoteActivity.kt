package com.example.mobileteamproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.mobileteamproject.databinding.ActivityBookNoteBinding

class BookNoteActivity : AppCompatActivity() {
    lateinit var binding: ActivityBookNoteBinding
    var booktitle: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Note"
        booktitle = intent.getStringExtra("title")

        var data = mutableListOf<String>()
        var rating = mutableListOf<Float>()

        //db 열고 데이터 읽기
        val db = openOrCreateDatabase("readdb", Context.MODE_PRIVATE, null)
        var cursor = db.rawQuery("select title from BOOK_TB where title = '${booktitle}'", null)
        data.clear()
        while (cursor.moveToNext()) {
            data.add(cursor.getString(0))
        }
        binding.textview.text = data[0]
        cursor = db.rawQuery("select rate from BOOK_TB where title = '${booktitle}'", null)
        rating.clear()
        while (cursor.moveToNext()) {
            rating.add(cursor.getFloat(0))
        }
        binding.ratingBar.rating = rating[0]
        db.close()

        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser -> run {
            val db = openOrCreateDatabase("readdb", Context.MODE_PRIVATE, null)
            db.execSQL("update BOOK_TB set rate = ${rating} where title = ?", arrayOf(booktitle))
            db.close()
        } }

        val note = getNoteFromDb()
        val fragment = ViewFragment()
        setDataAtFragment(fragment, note)

        binding.button.setOnClickListener {
            if (binding.button.text.toString().equals("Edit")) {
                binding.button.text = "OK"
                val data = getNoteFromDb()
                val fragment = EditFragment()
                setDataAtFragment(fragment, data)
            } else {
                binding.button.text = "Edit"
                val data = getNoteFromDb()
                val fragment = ViewFragment()
                setDataAtFragment(fragment, data)
            }
        }
    }
    fun getNoteFromDb(): String {
        var data = mutableListOf<String>()
        val db = openOrCreateDatabase("readdb", Context.MODE_PRIVATE, null)
        val cursor = db.rawQuery("select note from BOOK_TB where title = '${booktitle}'", null)
        data.clear()
        while (cursor.moveToNext()) {
            data.add(cursor.getString(0))
        }
        return data[0]
    }
    fun setDataAtFragment(fragment: Fragment, data: String) {
        val bundle = Bundle()
        bundle.putString("data", data)
        fragment.arguments = bundle
        setFragment(fragment)
    }
    fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_content, fragment)
        transaction.commit()
    }
    fun receiveData(data: String) {
        val db = openOrCreateDatabase("readdb", Context.MODE_PRIVATE, null)
        db.execSQL("update BOOK_TB set note = '${data}' where title = '${booktitle}'")
        db.close()
    }
}