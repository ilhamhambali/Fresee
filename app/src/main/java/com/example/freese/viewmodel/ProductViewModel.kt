package com.example.freese.viewmodel

import AddProductResponse
import ProductResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freese.data.remote.api.response.TransactionActionResponse
import com.example.freese.repository.ProductRepository
import com.example.freese.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
   private val repository: ProductRepository
) : ViewModel() {

   private val _allProductsState = MutableStateFlow<Result<List<ProductResponse>>?>(null)
   val allProductsState: StateFlow<Result<List<ProductResponse>>?> = _allProductsState.asStateFlow()

   private val _myProductsState = MutableStateFlow<Result<List<ProductResponse>>?>(null)
   val myProductsState: StateFlow<Result<List<ProductResponse>>?> = _myProductsState.asStateFlow()

   private val _addProductState = MutableStateFlow<Result<AddProductResponse>?>(null)
   val addProductState: StateFlow<Result<AddProductResponse>?> = _addProductState.asStateFlow()

   private val _updateProductState = MutableStateFlow<Result<AddProductResponse>?>(null)
   val updateProductState: StateFlow<Result<AddProductResponse>?> = _updateProductState.asStateFlow()

   private val _deleteProductState = MutableStateFlow<Result<TransactionActionResponse>?>(null)
   val deleteProductState: StateFlow<Result<TransactionActionResponse>?> = _deleteProductState.asStateFlow()

   fun deleteProduct(id: Int) {
      viewModelScope.launch {
         repository.deleteProduct(id).collect { result ->
            _deleteProductState.value = result
         }
      }
   }

   fun resetDeleteState() {
      _deleteProductState.value = null
   }

   fun getAllProducts(search: String? = null) {
      viewModelScope.launch {
         repository.getAllProducts(search).collect { result ->
            _allProductsState.value = result
         }
      }
   }

   fun getMyProducts() {
      viewModelScope.launch {
         repository.getMyProducts().collect { result ->
            _myProductsState.value = result
         }
      }
   }

   fun addProduct(name: RequestBody, price: RequestBody, stock: RequestBody, unit: RequestBody, // <-- Tambahan: unit
                  category: RequestBody, description: RequestBody, image: MultipartBody.Part) {
      viewModelScope.launch {
         repository.addProduct(name, price, stock, unit, category, description, image).collect { result ->
            _addProductState.value = result
         }
      }
   }

   fun updateProduct(id: Int, name: RequestBody, price: RequestBody, stock: RequestBody, unit: RequestBody, // <-- Tambahan: unit
                     category: RequestBody, description: RequestBody, image: MultipartBody.Part?) {
      viewModelScope.launch {
         repository.updateProduct(id, name, price, stock, unit, category, description, image).collect { result ->
            _addProductState.value = result
         }
      }
   }
}