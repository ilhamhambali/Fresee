package com.example.freese.ui.slider

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.freese.R
import com.example.freese.databinding.ActivityIntroSliderBinding
import com.example.freese.ui.main.MainActivity
class IntroSliderActivity : AppCompatActivity() {

   // Deklarasikan objek binding. Ini akan menggantikan semua variabel view individu.
   private lateinit var binding: ActivityIntroSliderBinding

   private lateinit var adapter: SliderAdapter
   private var size: Int = 0

   private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
      override fun onPageSelected(position: Int) {
         super.onPageSelected(position)
         addDots(size, position)

         if (position == 2) {
            Handler(Looper.getMainLooper()).postDelayed({
               val intent = Intent(this@IntroSliderActivity, MainActivity::class.java)
               intent.putExtra("linkss", "https://hssakingamapp.blogspot.com/")
               startActivity(intent)
               finish()
            }, 5000)
         }
      }


      override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

      override fun onPageScrollStateChanged(state: Int) {}
   }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      // Inflate layout menggunakan ViewBinding dan atur sebagai content view
      binding = ActivityIntroSliderBinding.inflate(layoutInflater)
      setContentView(binding.root)

      val preferences: SharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
      val firstTime = preferences.getString("FirstInstall", "")

      if (firstTime == "Yes") {
         val intent = Intent(this@IntroSliderActivity, MainActivity::class.java)
         intent.putExtra("linkss", "https://hssakingamapp.blogspot.com/")
         startActivity(intent)
         finish()
      } else {
         preferences.edit().apply {
            putString("FirstInstall", "Yes")
            apply()
         }
      }

      // Akses tombol 'skip' melalui objek binding
      binding.idBtnSkip.setOnClickListener {
         val intent = Intent(this@IntroSliderActivity, MainActivity::class.java)
         startActivity(intent)
         finish()
      }
      binding.idBtnNext.setOnClickListener {
         val currentPage = binding.idViewPager.currentItem

         if (currentPage < size - 1) {
            binding.idViewPager.currentItem = currentPage + 1
         } else {
            val intent = Intent(this@IntroSliderActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
         }
      }

      // BENAR
      val sliderItems = listOf(
         SliderItem("", "Thank you for installing our App", R.drawable.image_login),
         SliderItem("", "The School that inspires you",  R.drawable.image_signup),
         SliderItem("", "Online Registration, E-Content, Attendance Monitoring, Results, School Profile, & School Events etc",  R.drawable.image_welcome)
      )
      adapter = SliderAdapter(sliderItems)

      // Akses ViewPager melalui objek binding
      binding.idViewPager.adapter = adapter

      size = sliderItems.size

      addDots(size, 0)

      binding.idViewPager.registerOnPageChangeCallback(pageChangeCallback)
   }

   private fun addDots(size: Int, pos: Int) {
      if (size <= 0) return

      // Hapus semua view yang ada sebelumnya
      binding.idLLDots.removeAllViews()

      // Buat ImageView untuk setiap halaman
      val dots = Array(size) { ImageView(this) }

      for (i in 0 until size) {
         dots[i] = ImageView(this)

         // Pilih drawable berdasarkan posisi
         if (i == pos) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_active))
         } else {
            dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dot_inactive))
         }

         // Atur margin agar ada jarak antar titik
         val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
         ).apply {
            setMargins(8, 0, 8, 0) // Margin kiri, atas, kanan, bawah
         }
         dots[i].layoutParams = params

         // Tambahkan ImageView ke LinearLayout
         binding.idLLDots.addView(dots[i])
      }
   }
}