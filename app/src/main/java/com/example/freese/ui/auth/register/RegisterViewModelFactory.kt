package com.example.freese.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.freese.data.repository.UserRepository

   class RegisterViewModelFactory(private val authRepository: UserRepository) : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T {
         if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(authRepository) as T
         }
         throw IllegalArgumentException("Unknown ViewModel class")
      }
   }
