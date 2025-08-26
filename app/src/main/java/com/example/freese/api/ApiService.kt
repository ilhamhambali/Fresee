package com.example.freese.api

import com.example.freese.api.response.AuthResponse
import com.example.freese.api.response.ScanResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
   // Kelas data untuk register request


   // Kelas data untuk login request

   data class RegisterRequest(
      val username: String,
      val email: String,
      val password: String,
      val phoneNumber: String
   )
   @POST("auth/register")
   suspend fun register(
      @Body registerRequest: RegisterRequest
   ): AuthResponse



   data class LoginRequest(
      val username: String,
      val password: String
   )
   @POST("auth/login")
   suspend fun login(
      @Body loginRequest: LoginRequest
   ): AuthResponse



   @GET("auth/profile")
      suspend fun getProfile(): AuthResponse



   @Multipart
   @POST("/predict")
   suspend fun postImageScan(
      @Part img: MultipartBody.Part
   ): ScanResponse
}