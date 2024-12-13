package com.example.freese.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.freese.data.repository.UserRepository

class MainViewModelFactory(private val authRepository: UserRepository) : ViewModelProvider.Factory {
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
         @Suppress("UNCHECKED_CAST")
         return MainViewModel(authRepository) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class")
   }
}