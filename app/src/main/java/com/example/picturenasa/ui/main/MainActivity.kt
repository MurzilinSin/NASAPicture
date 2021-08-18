package com.example.picturenasa.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.picturenasa.R
import com.example.picturenasa.ui.main.view.settings.SettingsFragment
import com.example.picturenasa.ui.main.view.main.MainFragment
import com.example.picturenasa.ui.main.view.mars.MarsRoverPhotoFragment

import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.bottom_view_settings -> {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, SettingsFragment())
                            .addToBackStack("")
                            .commit()
                    true
                }
                R.id.bottom_view_mars -> {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, MarsRoverPhotoFragment())
                            .addToBackStack("")
                            .commit()
                    true
                }
                R.id.bottom_view_picture -> {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.container, MainFragment())
                            .addToBackStack("")
                            .commit()
                    true
                }
                else -> false
            }
        }
    }
}