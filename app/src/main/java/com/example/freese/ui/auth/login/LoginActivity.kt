package com.example.freese.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.freese.GenericViewModelFactory
import com.example.freese.databinding.ActivityLoginBinding
import com.example.freese.di.DependencyProvider
import com.example.freese.ui.main.MainActivity
import com.example.freese.ui.auth.register.RegisterActivity
import com.example.freese.ui.auth.AuthViewModel

class LoginActivity : AppCompatActivity() {

   private lateinit var viewModel: AuthViewModel
   private lateinit var binding: ActivityLoginBinding

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityLoginBinding.inflate(layoutInflater)
      setContentView(binding.root)

      binding.idLewati.setOnClickListener {
         val intent = Intent(this, MainActivity::class.java)
         startActivity(intent)
         finish()
      }

      val repository = DependencyProvider.provideUserRepository(this)
      val factory = GenericViewModelFactory(AuthViewModel::class.java) {
         AuthViewModel(repository)
      }
      viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

      setupObserver()
      setupAction()
      playAnimation()
   }

   private fun setupObserver() {
      viewModel.loginResult.observe(this) { result ->
         result.onSuccess { response ->
            Toast.makeText(this, "Login berhasil: ${response.message}", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
         }
         result.onFailure { throwable ->
            Toast.makeText(this, "Login gagal: ${throwable.message}", Toast.LENGTH_SHORT).show()
         }
      }
   }

   private fun setupAction() {
      binding.loginButton.setOnClickListener {
         val username = binding.edLoginEmail.text.toString().trim()
         val password = binding.edLoginPassword.text.toString().trim()

         if (username.isNotEmpty() && password.isNotEmpty()) {
            // Cukup panggil fungsi login di ViewModel
            viewModel.login(username, password)
         } else {
            Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
         }
      }
      binding.registerButton.setOnClickListener {
         val intent = Intent(this, RegisterActivity::class.java)
         intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
         startActivity(intent)
         finish()
      }

   }
   private fun playAnimation() {


      val emailEditTextLayout =
         ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
      val passwordTextView =
         ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
      val passwordEditTextLayout =
         ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
      val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

      AnimatorSet().apply {
         playSequentially(

            emailEditTextLayout,
            passwordTextView,
            passwordEditTextLayout,
            login
         )
         startDelay = 100
      }.start()
   }
}