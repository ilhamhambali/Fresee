package com.example.freese.retrofit


import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "http://34.101.160.135:4600/"

object ScanApiConfig {
   fun getApiService(): ApiService {
      val loggingInterceptor =
         HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
      val client = OkHttpClient.Builder()
         .addInterceptor(loggingInterceptor)
         .build()
      val retrofit = Retrofit.Builder()
         .baseUrl(BASE_URL)
         .addConverterFactory(GsonConverterFactory.create())
         .client(client)
         .build()
      return retrofit.create(ApiService::class.java)
   }
}