package com.example.freese.ui.auth.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.freese.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
      private lateinit var binding: ActivityRegisterBinding

      override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         binding = ActivityRegisterBinding.inflate(layoutInflater)
         setContentView(binding.root)

         // 1. Panggil Adapter yang baru dibuat
         val sectionsPagerAdapter = SectionsPagerAdapter(this)

         // 2. Pasang Adapter ke ViewPager
         binding.viewPager.adapter = sectionsPagerAdapter

         // 3. Hubungkan Tab dengan ViewPager (Judul Tab)
         TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = if (position == 0) "Penjual" else "Pembeli"
         }.attach()

         supportActionBar?.elevation = 0f
      }
   }























//   private lateinit var binding: ActivityRegisterBinding
//   private val viewModel: AuthViewModel by viewModels()
//
//   override fun onCreate(savedInstanceState: Bundle?) {
//      super.onCreate(savedInstanceState)
//      binding = ActivityRegisterBinding.inflate(layoutInflater)
//      setContentView(binding.root)
//
//      setupObservers()
//
//      binding.registerButton.setOnClickListener {
//         val email = binding.edRegisterEmail.text.toString().trim()
//         val password = binding.edRegisterPassword.text.toString().trim()
//
//         if (email.isEmpty() || password.isEmpty()) {
//            Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
//            return@setOnClickListener
//         }
//         viewModel.registerUser(AuthRequest(email, password))
//      }
//
////      playAnimation()
//
//   }
//
//   private fun setupObservers() {
//      lifecycleScope.launch {
//         viewModel.registerState.collect { result ->
//            when (result) {
//               is Result.Loading -> {
//                  binding.progressBar.visibility = View.VISIBLE
//               }
//               is Result.Success -> {
//                  binding.progressBar.visibility = View.GONE
//                  Toast.makeText(this@RegisterActivity, "Registrasi berhasil! Silakan login.", Toast.LENGTH_LONG).show()
//                  finish() // Kembali ke halaman login
//               }
//               is Result.Error -> {
//                  binding.progressBar.visibility = View.GONE
//                  Toast.makeText(this@RegisterActivity, result.message, Toast.LENGTH_LONG).show()
//               }
//               null -> { /* Initial state */ }
//            }
//         }
//      }
//   }

//   private fun playAnimation() {
//
//      val nameTextView =
//         ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(1000)
//      val nameEditTextLayout =
//         ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(1000)
//      val emailTextView =
//         ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(1000)
//      val emailEditTextLayout =
//         ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(1000)
//      val passwordTextView =
//         ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(1000)
//      val passwordEditTextLayout =
//         ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(1000)
//      val signup = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(1000)
//
//
//      AnimatorSet().apply {
//         playSequentially(
//            nameTextView,
//            nameEditTextLayout,
//            emailTextView,
//            emailEditTextLayout,
//            passwordTextView,
//            passwordEditTextLayout,
//            signup
//         )
//         startDelay = 100
//      }.start()
//   }
