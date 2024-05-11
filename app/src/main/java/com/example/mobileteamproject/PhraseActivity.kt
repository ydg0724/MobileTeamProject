package com.example.mobileteamproject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mobileteamproject.databinding.ActivityPhraseBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.LineNumberReader
import java.security.SecureRandom
import kotlin.random.Random

class PhraseActivity : AppCompatActivity() {
    lateinit var binding: ActivityPhraseBinding
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhraseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "명언"

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
            Toast.makeText(this, "현재 속한 페이지", Toast.LENGTH_SHORT).show()
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

        //명언 파일 읽기
        var inputStream = resources.openRawResource(R.raw.phrase)
        var isr = InputStreamReader(inputStream, "UTF-8")
        var br = BufferedReader(isr)
        val sr = SecureRandom()

        //전체 줄 수 파악하고 랜덤
        var phrase: String? = ""
        var lineCount = 0
        while (br.readLine() != null) {
            lineCount++
        }
        var num = sr.nextInt(lineCount)
        inputStream.close()
        br.close()

        //파일 처음부터 다시 읽기
        inputStream = resources.openRawResource(R.raw.phrase)
        isr = InputStreamReader(inputStream, "UTF-8")
        br = BufferedReader(isr)

        //랜덤하게 출력
        for (i in 0..num)
            phrase = br.readLine()
        binding.textview.text = phrase

        //버튼 이벤트
        binding.next.setOnClickListener {
            num = sr.nextInt(lineCount)
            for (i in 0..num)
                phrase = br.readLine()
            if (phrase == null) {
                inputStream.close()
                br.close()
                inputStream = resources.openRawResource(R.raw.phrase)
                isr = InputStreamReader(inputStream, "UTF-8")
                br = BufferedReader(isr)
                num = sr.nextInt(lineCount)
                for (i in 0..num)
                    phrase = br.readLine()
            }
            binding.textview.text = phrase
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}