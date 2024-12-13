package com.example.freese.retrofit

import com.example.freese.data.AuthResponse
import com.example.freese.data.ProductsItem
import com.example.freese.data.ScanResponse
import com.example.freese.data.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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