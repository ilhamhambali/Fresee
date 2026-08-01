package com.example.freese.repository

import com.example.freese.data.local.pref.SessionManager
import com.example.freese.data.remote.api.ApiService
import com.example.freese.data.remote.api.response.ChangePasswordRequest
import com.example.freese.data.remote.api.response.EditProfileResponse
import com.example.freese.data.remote.api.response.TransactionActionResponse
import com.example.freese.utils.Result
import com.example.freese.data.remote.api.response.UserProfileResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.MultipartBody
import okhttp3.RequestBody

@Singleton
class UserRepository @Inject constructor(
   private val apiService: ApiService,
   private val sessionManager: SessionManager
) {

   // Fungsi Get Profile
   fun getProfile(): Flow<Result<UserProfileResponse>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.getProfile()
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal mengambil profil: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun editProfile(
      fullName: RequestBody,
      phone: RequestBody,
      address: RequestBody,
      avatar: MultipartBody.Part?
   ): Flow<Result<EditProfileResponse>> = flow {
      emit(Result.Loading)
      try {
         // Parameter farmName sudah dihapus dari panggilan ini
         val response = apiService.editProfile(fullName, phone, address, avatar)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal update profil: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun changePassword(request: ChangePasswordRequest): Flow<Result<TransactionActionResponse>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.changePassword(request)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal ganti password: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun saveUserRole(role: String) {
      sessionManager.saveUserRole(role)
   }

   // (Opsional) Fungsi untuk mengambil role jika nanti dibutuhkan di ViewModel
   fun fetchUserRole(): String? {
      return sessionManager.fetchUserRole()
   }
}