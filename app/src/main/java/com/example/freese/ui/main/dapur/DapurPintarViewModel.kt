package com.example.freese.ui.main.dapur


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freese.data.remote.model.InventarisBuah
import com.example.freese.repository.DapurRepository
import com.example.freese.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Langkah 4 (Kotlin): Buat DapurViewModel
@HiltViewModel
class DapurPintarViewModel @Inject constructor(private val repository: DapurRepository) : ViewModel() {

   private val _inventarisState = MutableStateFlow<Result<List<InventarisBuah>>>(Result.Loading)
   val inventarisState: StateFlow<Result<List<InventarisBuah>>> = _inventarisState

   init {
      fetchInventaris()
   }

   fun fetchInventaris() {
      viewModelScope.launch {
         repository.getInventarisBuah().collect {
            _inventarisState.value = it
         }
      }
   }

   // Nanti bisa ditambahkan fungsi untuk menambah buah
}