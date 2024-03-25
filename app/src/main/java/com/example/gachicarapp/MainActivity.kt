package com.example.gachicarapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GachiCar)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            // 모든 아이콘을 초기 상태로 설정
            resetIconsToOffState()

            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_voice -> {
                    selectedFragment = VoiceFragment()
                    item.icon = ContextCompat.getDrawable(this, R.drawable.on_voice_ic)
                }
                R.id.nav_stats -> {
                    selectedFragment = StatsFragment()
                    item.icon = ContextCompat.getDrawable(this, R.drawable.on_stats_ic)
                }
                R.id.nav_home -> {
                    selectedFragment = HomeFragment()
                }
                R.id.nav_notifications -> {
                    selectedFragment = NotificationsFragment()
                    item.icon = ContextCompat.getDrawable(this, R.drawable.on_alarm_ic)
                }
                R.id.nav_profile -> {
                    selectedFragment = ProfileFragment()
                    item.icon = ContextCompat.getDrawable(this, R.drawable.on_profile_ic)
                }
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit()
            }

            true
        }

        // 기본으로 선택된 탭 설정
        bottomNavigationView.selectedItemId = R.id.nav_home
    }

    private fun resetIconsToOffState() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.menu.apply {
            findItem(R.id.nav_voice).icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.off_voice_ic)
            findItem(R.id.nav_stats).icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.off_stats_ic)
            findItem(R.id.nav_notifications).icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.off_alarm_ic)
            findItem(R.id.nav_profile).icon = ContextCompat.getDrawable(this@MainActivity, R.drawable.off_profile_ic)
        }
    }
}