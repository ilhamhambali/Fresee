package com.example.freese.ui.main.scan
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.freese.data.repository.ScanRepository
import java.lang.IllegalArgumentException

class ScanViewModelFactory(private val repository: ScanRepository) : ViewModelProvider.Factory {
   @Suppress("UNCHECKED_CAST")
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
         return ScanViewModel(repository) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
   }
}
