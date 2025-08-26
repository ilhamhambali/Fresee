package com.example.freese.ui.splash


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.freese.R
import com.example.freese.ui.slider.IntroSliderActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_splash)


      Handler(Looper.getMainLooper()).postDelayed({
         startActivity(Intent(this, IntroSliderActivity::class.java))
         finish()
      }, 3000)


   }
}

