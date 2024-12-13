package com.example.freese.ui.splash


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.freese.DependencyProvider
import com.example.freese.GenericViewModelFactory
import com.example.freese.R
import com.example.freese.ui.auth.AuthViewModel
import com.example.freese.ui.auth.AuthViewModelFactory
import com.example.freese.ui.auth.login.LoginActivity
import com.example.freese.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

   private lateinit var viewModel: AuthViewModel

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_splash)

      viewModel = ViewModelProvider(
         this,
         GenericViewModelFactory { AuthViewModel(DependencyProvider.provideUserRepository(this)) }
      )[AuthViewModel::class.java]

      viewModel.getSession().observe(this) { user ->
         Log.d("ScanActivity", "Token: ${user.token}")

            // Token sudah ada, lanjutkan ke MainActivity
            if (user.isLogin) {
               startActivity(Intent(this, MainActivity::class.java))
            } else {
               startActivity(Intent(this, LoginActivity::class.java))
            }
         
         finish()
      }

   }
}

