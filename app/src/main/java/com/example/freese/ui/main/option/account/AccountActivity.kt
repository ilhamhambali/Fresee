package com.example.freese.ui.main.option.account

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.freese.DependencyProvider
import com.example.freese.GenericViewModelFactory
import com.example.freese.R
import com.example.freese.databinding.ActivityAccountBinding
import com.example.freese.ui.auth.AuthViewModel

class AccountActivity : AppCompatActivity() {

   private lateinit var binding: ActivityAccountBinding
   private lateinit var viewModel: AuthViewModel


   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityAccountBinding.inflate(layoutInflater)
      setContentView(binding.root)

      viewModel = ViewModelProvider(
         this,
         GenericViewModelFactory { AuthViewModel(DependencyProvider.provideUserRepository(this)) }
      )[AuthViewModel::class.java]
      viewModel.profile.observe(this) { result ->
         result.onSuccess { user ->
            binding.tvName.text = user.username
            binding.tvEmail.text = user.email
            binding.tvPhone.text = user.phoneNumber
            binding.tvAddress.text = "Alamat tidak tersedia" // Sesuaikan jika ada data alamat
         }
         result.onFailure { exception ->
            Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
         }
      }


      // Meminta data profil dari ViewModel
      viewModel.fetchProfile()
   }
}