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
import androidx.fragment.app.Fragment
import com.example.mobileteamproject.databinding.ActivitySportBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class SportActivity : AppCompatActivity() {
    lateinit var binding: ActivitySportBinding
    lateinit var toggle: ActionBarDrawerToggle
    val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.menu_timer -> {
                replaceFragment(TimerFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_home -> {
                replaceFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.menu_calendar -> {
                replaceFragment(CalendarFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "운동"
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
            Toast.makeText(this, "현재 속한 페이지", Toast.LENGTH_SHORT).show()
            binding.drawer.close()
        }
        binding.menu5.setOnClickListener {
            startActivity(Intent(this, ReadActivity::class.java))
            binding.drawer.close()
        }

        // Set up the bottom navigation view
        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // Set the initial fragment
        replaceFragment(HomeFragment())

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}