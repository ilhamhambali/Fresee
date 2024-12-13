package com.example.freese.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.freese.DependencyProvider
import com.example.freese.GenericViewModelFactory
import com.example.freese.databinding.ActivityLoginBinding
import com.example.freese.ui.main.MainActivity
import com.example.freese.ui.auth.register.RegisterActivity
import com.example.freese.data.pref.UserPreference
import com.example.freese.data.repository.UserRepository
import com.example.freese.ui.auth.AuthViewModel
import com.example.freese.ui.auth.AuthViewModelFactory

class LoginActivity : AppCompatActivity() {

   private lateinit var viewModel: AuthViewModel
   private lateinit var binding: ActivityLoginBinding

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityLoginBinding.inflate(layoutInflater)
      setContentView(binding.root)


      setupAction()
      playAnimation()
   }

   private fun playAnimation() {

      val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
      val message =
         ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
      val emailTextView =
         ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
      val emailEditTextLayout =
         ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
      val passwordTextView =
         ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
      val passwordEditTextLayout =
         ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
      val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

      AnimatorSet().apply {
         playSequentially(
            title,
            message,
            emailTextView,
            emailEditTextLayout,
            passwordTextView,
            passwordEditTextLayout,
            login
         )
         startDelay = 100
      }.start()
   }

   private fun setupAction() {
      binding.loginButton.setOnClickListener {
         val username = binding.edLoginEmail.text.toString().trim()
         val password = binding.edLoginPassword.text.toString().trim()


      viewModel = ViewModelProvider(
         this,
         GenericViewModelFactory { AuthViewModel(DependencyProvider.provideUserRepository(this)) }
      )[AuthViewModel::class.java]
         if (username.isNotEmpty() && password.isNotEmpty()) {
            viewModel.login(username, password)

            viewModel.loginResult.observe(this) { result ->
               result.onSuccess { response ->
                  Toast.makeText(
                     this,
                     "Login berhasil: ${response.message}",
                     Toast.LENGTH_SHORT
                  ).show()
                  val intent = Intent(this, MainActivity::class.java)
                  intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                  startActivity(intent)
                  finish()
               }

               result.onFailure { throwable ->
                  Toast.makeText(
                     this,
                     "Login gagal: ${throwable.message}",
                     Toast.LENGTH_SHORT
                  ).show()
               }
            }
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
}