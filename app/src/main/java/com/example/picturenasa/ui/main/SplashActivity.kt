package com.example.picturenasa.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.example.picturenasa.R

class SplashActivity : AppCompatActivity() {

    private val handler = Handler(Looper.myLooper()!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val imageView = findViewById<AppCompatImageView>(R.id.image_splash)
        imageView.animate()
            .rotationBy(750f)
            .setInterpolator(LinearInterpolator())
            .duration = 4000
        println("Tut mi")
        handler.postDelayed( {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        },3000)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}