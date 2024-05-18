package com.example.mobileteamproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileteamproject.databinding.ActivityAddBookBinding
import com.example.mobileteamproject.databinding.ActivityBookBinding
import java.io.File

class AddBookActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "책 추가"

        val path: File = getDatabasePath("readdb")

        binding.addBtn.setOnClickListener {
            val db = openOrCreateDatabase("readdb", Context.MODE_PRIVATE, null)
            db.execSQL("insert into BOOK_TB(title) values(?)",
                arrayOf(binding.editview.text.toString()))
            db.execSQL("update BOOK_TB set note = '' where title = ?",
                arrayOf(binding.editview.text.toString()))
            db.close()
            finish()
            startActivity(Intent(this, BookActivity::class.java))
        }
    }
}