package com.example.freese.ui.main.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freese.api.response.ScanResponse
import com.example.freese.data.repository.ScanRepository
import kotlinx.coroutines.launch
import java.io.File

class ScanViewModel(private val repository: ScanRepository) : ViewModel() {

   private val _scanResult = MutableLiveData<ScanResponse>()
   val scanResult: LiveData<ScanResponse> get() = _scanResult

   private val _isLoading = MutableLiveData<Boolean>()
   val isLoading: LiveData<Boolean> get() = _isLoading

   fun setScanResult(result: ScanResponse) {
      _scanResult.value = ScanResponse(message=result.message)
   }

   // Fungsi upload image dan mengirim response ke model
   fun uploadImage(file: File) {
      viewModelScope.launch {
         _isLoading.value = true
         try {
            val response = repository.uploadImage(file)
            _scanResult.value = response  // Pastikan ini berhasil di-set
            setScanResult(response)
         } catch (e: Exception) {
         } finally {
            _isLoading.value = false
         }
      }
   }
}

