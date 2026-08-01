package com.example.freese.repository

import android.util.Log
import com.example.freese.data.remote.api.ApiService
import com.example.freese.data.remote.api.request.LoginRequest
import com.example.freese.data.remote.api.request.RegisterRequest
import com.example.freese.data.remote.api.response.LoginResponse
import com.example.freese.data.remote.api.response.RegisterResponse
import com.example.freese.data.local.pref.SessionManager
import com.example.freese.utils.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
   private val apiService: ApiService,
   private val sessionManager: SessionManager
) {

   fun login(loginRequest: LoginRequest): Flow<Result<LoginResponse>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.loginUser(loginRequest)

         if (response.isSuccessful && response.body() != null) {
            sessionManager.saveAuthToken(response.body()!!.token)
            Log.d("AuthRepository", "Token saved: ${response.body()!!.token}")
            emit(Result.Success(response.body()!!))

         } else {
            emit(Result.Error("Login failed: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "An unknown error occurred"))
      }
   }

   fun register(
      email: RequestBody,
      password: RequestBody,
      fullName: RequestBody,
      phone: RequestBody,
      role: RequestBody,
      photo: MultipartBody.Part?
   ): Flow<Result<RegisterResponse>> = flow {
      emit(Result.Loading)
      try {
         // Panggil apiService dengan parameter Multipart yang baru
         val response = apiService.registerUser(email, password, fullName, phone, role, photo)

         delay(2000)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Register failed: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "An unknown error occurred"))
      }
   }

   fun logout() {
      sessionManager.clearAuthToken()
   }

   fun isLoggedIn(): Boolean {
      return sessionManager.fetchAuthToken() != null
   }
}