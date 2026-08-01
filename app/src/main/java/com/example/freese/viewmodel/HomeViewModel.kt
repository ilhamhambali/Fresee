package com.example.freese.viewmodel

import ProductResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freese.data.remote.model.ProductModel
import com.example.freese.repository.ProductRepository
import com.example.freese.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
   private val repository: ProductRepository
) : ViewModel() {

   // Ubah tipe datanya ke ProductModel
   private val _productsState = MutableStateFlow<Result<List<ProductResponse>>?>(null)
   val productsState: StateFlow<Result<List<ProductResponse>>?> = _productsState

   init {
      fetchProducts()
   }

   fun fetchProducts() {
      viewModelScope.launch {
         repository.getAllProducts().collect { result ->
            _productsState.value = result
         }
      }
   }
}