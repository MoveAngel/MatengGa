package com.exam.matengga

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exam.matengga.view.camera.CameraFragment
import com.exam.matengga.view.history.HistoryFragment
import com.exam.matengga.view.home.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
            bottomNavigationView.selectedItemId = R.id.nav_home
        }

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                    true
                }
                R.id.nav_history -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HistoryFragment())
                        .commit()
                    true
                }
                R.id.nav_camera -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CameraFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}