package com.example.freese.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.freese.data.repository.UserRepository

class AuthViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
   @Suppress("UNCHECKED_CAST")
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
         return AuthViewModel(userRepository) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class")
   }
}
