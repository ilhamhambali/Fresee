package com.example.freese.repository
import com.example.freese.data.remote.api.ApiService
import com.example.freese.utils.Result
import com.example.freese.data.remote.api.response.AddToCartRequest
import com.example.freese.data.remote.api.response.AddToCartResponse
import com.example.freese.data.remote.api.response.CartItemResponse
import com.example.freese.data.remote.api.response.SelectCartItemRequest
import com.example.freese.data.remote.api.response.UpdateQuantityRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor(
   private val apiService: ApiService
) {
   fun addToCart(request: AddToCartRequest): Flow<Result<AddToCartResponse>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.addToCart(request)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal menambah keranjang: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }
   fun getCart(): Flow<Result<List<CartItemResponse>>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.getCart()
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal memuat keranjang: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun selectCartItem(cartItemId: Int, isSelected: Boolean): Flow<Result<AddToCartResponse>> = flow {
      emit(Result.Loading)
      try {
         val request = SelectCartItemRequest(isSelected)
         val response = apiService.selectCartItem(cartItemId, request)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal update item: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun updateCartQuantity(cartItemId: Int, quantity: Int): Flow<Result<AddToCartResponse>> = flow {
      emit(Result.Loading)
      try {
         val request = UpdateQuantityRequest(quantity)
         val response = apiService.updateCartQuantity(cartItemId, request)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal mengubah jumlah: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun deleteCartItem(cartItemId: Int): Flow<Result<AddToCartResponse>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.deleteCartItem(cartItemId)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal menghapus item: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }
}