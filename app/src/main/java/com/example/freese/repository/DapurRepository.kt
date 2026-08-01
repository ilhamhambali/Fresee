package com.example.freese.repository


import com.example.freese.data.remote.api.ApiService
import com.example.freese.data.remote.model.InventarisBuah
import com.example.freese.data.remote.model.TambahBuahRequest
import com.example.freese.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

// Langkah 4 (Kotlin): Buat DapurRepository
@Singleton
class DapurRepository @Inject constructor(private val apiService: ApiService) {

   fun getInventarisBuah(): Flow<Result<List<InventarisBuah>>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.getInventaris()
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal mengambil data: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan"))
      }
   }

   fun tambahBuah(request: TambahBuahRequest): Flow<Result<InventarisBuah>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.tambahBuah(request)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal menambah buah: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan"))
      }
   }
}