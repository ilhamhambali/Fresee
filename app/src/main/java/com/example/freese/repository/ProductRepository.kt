package com.example.freese.repository

import AddProductResponse
import ProductResponse
import com.example.freese.data.remote.api.ApiService
import com.example.freese.data.remote.api.response.TransactionActionResponse
import com.example.freese.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
   private val apiService: ApiService
) {
   fun getAllProducts(search: String? = null): Flow<Result<List<ProductResponse>>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.getAllProducts(search)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal memuat produk: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun getMyProducts(): Flow<Result<List<ProductResponse>>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.getMyProducts()
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal memuat produk Anda: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun addProduct(
      name: RequestBody, price: RequestBody, stock: RequestBody, unit: RequestBody, // <-- Tambahan: unit
      category: RequestBody, description: RequestBody, image: MultipartBody.Part
   ): Flow<Result<AddProductResponse>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.addProduct(name, price, stock, unit, category, description, image)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal menambah produk: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun updateProduct(
      id: Int, name: RequestBody, price: RequestBody, stock: RequestBody, unit: RequestBody, // <-- Tambahan: unit
      category: RequestBody, description: RequestBody, image: MultipartBody.Part?
   ): Flow<Result<AddProductResponse>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.updateProduct(id, name, price, stock, unit, category, description, image)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal update produk: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun deleteProduct(id: Int): Flow<Result<TransactionActionResponse>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.deleteProduct(id)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal menghapus produk: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }
}