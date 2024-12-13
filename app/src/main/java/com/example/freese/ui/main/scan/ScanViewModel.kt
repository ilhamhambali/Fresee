package com.example.freese.ui.main.scan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freese.data.ScanResponse
import com.example.freese.data.repository.ScanRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
            Log.d("ScanViewModel", "(ScanViewModel) Response received: ${response.message}")
         } catch (e: Exception) {
            Log.e("ScanViewModel", "uploadImage: ${e.message}")
         } finally {
            _isLoading.value = false
         }
      }
   }
}

