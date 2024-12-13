package com.example.freese.ui.auth.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.freese.DependencyProvider
import com.example.freese.GenericViewModelFactory
import com.example.freese.databinding.ActivityRegisterBinding
import com.example.freese.ui.auth.AuthViewModel
import com.example.freese.ui.auth.login.LoginActivity
import com.example.freese.ui.auth.login.LoginViewModelFactory
import com.example.freese.data.pref.UserPreference
import com.example.freese.data.repository.UserRepository
import com.example.freese.ui.auth.AuthViewModelFactory
import com.example.freese.ui.main.MainActivity

class RegisterActivity : AppCompatActivity() {
   private lateinit var binding: ActivityRegisterBinding
   private lateinit var viewModel: AuthViewModel

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityRegisterBinding.inflate(layoutInflater)
      setContentView(binding.root)

      setupView()
      playAnimation()

      setupAction()
      observeRegisterResult()
   }


   private fun observeRegisterResult() {
      viewModel.registerResult.observe(this) { result ->
         result.onSuccess { response ->
            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
         }

         result.onFailure { throwable ->
            // Jika registrasi gagal
            Toast.makeText(this, "Register failed: ${throwable.message}", Toast.LENGTH_SHORT).show()
         }
      }
   }

   private fun setupAction() {
      viewModel = ViewModelProvider(
         this,
         GenericViewModelFactory { AuthViewModel(
            DependencyProvider.provideUserRepository(this)) }
      )[AuthViewModel::class.java]
      binding.registerButton.setOnClickListener {
         val username = binding.edRegisterName.text.toString()
         val email = binding.edRegisterEmail.text.toString()
         val password = binding.edRegisterPassword.text.toString()
         val phoneNumber = binding.edRegisterNomor.text.toString()

         // Validasi input
         if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()&&phoneNumber.isNotEmpty()) {
            viewModel.register(username, email, password, phoneNumber)
         } else {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
         }
      }
      binding.loginButton.setOnClickListener {
         val intent = Intent(this, LoginActivity::class.java)
         intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
         startActivity(intent)
         finish()
      }
   }
   private fun setupView() {
      @Suppress("DEPRECATION")
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
         window.insetsController?.hide(WindowInsets.Type.statusBars())
      } else {
         window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
         )
      }
      supportActionBar?.hide()
   }
   private fun playAnimation() {
      val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
      val nameTextView =
         ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
      val nameEditTextLayout =
         ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
      val emailTextView =
         ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
      val emailEditTextLayout =
         ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
      val passwordTextView =
         ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
      val passwordEditTextLayout =
         ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
      val signup = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(100)


      AnimatorSet().apply {
         playSequentially(
            title,
            nameTextView,
            nameEditTextLayout,
            emailTextView,
            emailEditTextLayout,
            passwordTextView,
            passwordEditTextLayout,
            signup
         )
         startDelay = 100
      }.start()
   }
}