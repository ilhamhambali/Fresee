package com.example.freese.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.freese.data.remote.api.request.LoginRequest
import com.example.freese.databinding.ActivityLoginBinding
import com.example.freese.ui.main.MainActivity
import com.example.freese.ui.auth.register.RegisterActivity
import com.example.freese.ui.sellermain.SellerMainActivity
import com.example.freese.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.freese.utils.Result
import com.example.freese.viewmodel.UserViewModel

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

   private lateinit var binding: ActivityLoginBinding
   private val authViewModel: AuthViewModel by viewModels()
   private val userViewModel: UserViewModel by viewModels()

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityLoginBinding.inflate(layoutInflater)
      setContentView(binding.root)

      // Cek apakah pengguna sudah login
      if (authViewModel.isLoggedIn()) {
         goToMainActivity()
         return
      }

      setupObservers()

      binding.loginButton.setOnClickListener {
         val email = binding.edLoginEmail.text.toString().trim()
         val password = binding.edLoginPassword.text.toString().trim()

         if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return@setOnClickListener
         }
         authViewModel.loginUser(LoginRequest(email, password))
      }

      binding.registerButton.setOnClickListener {
         startActivity(Intent(this, RegisterActivity::class.java))
      }

   }

   private fun setupObservers() {
      lifecycleScope.launch {
         repeatOnLifecycle(Lifecycle.State.STARTED) {
            authViewModel.loginState.collect { result ->
               when (result) {
                  is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                  is Result.Success -> {
                     binding.progressBar.visibility = View.GONE
                     Toast.makeText(this@LoginActivity, "Login berhasil!", Toast.LENGTH_SHORT).show()
                     userViewModel.getProfile()
                  }
                  is Result.Error -> {
                     binding.progressBar.visibility = View.GONE
                     Toast.makeText(this@LoginActivity, result.message, Toast.LENGTH_LONG).show()
                  }
                  null -> { /* Initial state */ }
               }
            }
         }
      }
      lifecycleScope.launch {
         userViewModel.profileState.collect { result ->
            when (result) {
               is Result.Loading -> {
                  /* Biarkan loading tetap berputar */
               }
               is Result.Success -> {
                  // Sembunyikan loading
                  val role = result.data.role

                  // Simpan Role ke SessionManager
                  userViewModel.saveUserRole(role)

                  // ROUTING: Pisahkan arah tujuan berdasarkan Role
                  if (role == "seller") {
                     val intent = Intent(this@LoginActivity, SellerMainActivity::class.java)
                     startActivity(intent)
                  } else {
                     val intent = Intent(this@LoginActivity, MainActivity::class.java)
                     startActivity(intent)
                  }
                  finish() // Tutup halaman login
               }
               is Result.Error -> {
                  /* Jika gagal ambil profil, minta user login ulang atau coba lagi */
               }
               null -> {}
            }
         }
      }
   }

   private fun goToMainActivity(){
      startActivity(Intent(this, MainActivity::class.java))
      finish()
   }

}