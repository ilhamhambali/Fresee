package com.example.freese.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freese.data.remote.api.response.AddToCartRequest
import com.example.freese.data.remote.api.response.AddToCartResponse
import com.example.freese.data.remote.api.response.CartItemResponse
import com.example.freese.repository.CartRepository
import com.example.freese.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
   private val repository: CartRepository
) : ViewModel() {

   private val _addToCartState = MutableStateFlow<Result<AddToCartResponse>?>(null)
   val addToCartState: StateFlow<Result<AddToCartResponse>?> = _addToCartState.asStateFlow()

   private val _cartListState = MutableStateFlow<Result<List<CartItemResponse>>?>(null)
   val cartListState: StateFlow<Result<List<CartItemResponse>>?> = _cartListState.asStateFlow()

   private val _selectItemState = MutableStateFlow<Result<AddToCartResponse>?>(null)
   val selectItemState: StateFlow<Result<AddToCartResponse>?> = _selectItemState.asStateFlow()
   private val _updateItemState = MutableStateFlow<Result<AddToCartResponse>?>(null)
   val updateItemState: StateFlow<Result<AddToCartResponse>?> = _updateItemState.asStateFlow()

   fun updateCartQuantity(cartItemId: Int, quantity: Int) {
      viewModelScope.launch {
         repository.updateCartQuantity(cartItemId, quantity).collect { result ->
            _updateItemState.value = result
         }
      }
   }

   fun deleteCartItem(cartItemId: Int) {
      viewModelScope.launch {
         repository.deleteCartItem(cartItemId).collect { result ->
            _updateItemState.value = result // Kita bisa pakai state yang sama untuk trigger refresh
         }
      }
   }

   fun resetUpdateState() {
      _updateItemState.value = null
   }

   fun getCart() {
      viewModelScope.launch {
         repository.getCart().collect { result ->
            _cartListState.value = result
         }
      }
   }

   fun selectCartItem(cartItemId: Int, isSelected: Boolean) {
      viewModelScope.launch {
         repository.selectCartItem(cartItemId, isSelected).collect { result ->
            _selectItemState.value = result
         }
      }
   }
   fun addToCart(productId: Int, quantity: Int) {
      viewModelScope.launch {
         val request = AddToCartRequest(productId, quantity)
         repository.addToCart(request).collect { result ->
            _addToCartState.value = result
         }
      }
   }

   // Fungsi penting untuk me-reset state agar pesan sukses tidak muncul berulang
   // saat HP di-rotate (perubahan orientasi)
   fun resetState() {
      _addToCartState.value = null
   }
}