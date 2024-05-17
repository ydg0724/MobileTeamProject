package com.example.mobileteamproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileteamproject.databinding.ActivityAddBinding
import java.io.File

class AddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Add Todos"

        val path: File = getDatabasePath("tododb")

        binding.addbtn.setOnClickListener {
            val db = openOrCreateDatabase("tododb", Context.MODE_PRIVATE, null)
            db.execSQL("insert into TODO_TB(data) values(?)",
                arrayOf(binding.editview.text.toString()))
            db.close()
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}