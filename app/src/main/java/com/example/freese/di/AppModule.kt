package com.example.freese.di

import android.content.Context
import com.example.freese.data.remote.api.ApiService
import com.example.freese.utils.AuthInterceptor
import com.example.freese.data.local.pref.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
   //   private const val BASE_URL = "http://10.0.2.2:3000/"
   private const val BASE_URL_OFFLINE = "http://192.168.100.32:3000/"
   @Provides
   @Singleton
   fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient { // Minta Hilt untuk menyediakan AuthInterceptor
      val loggingInterceptor = HttpLoggingInterceptor().apply {
         level = HttpLoggingInterceptor.Level.BODY
      }
      return OkHttpClient.Builder()
         .addInterceptor(loggingInterceptor)
         .addInterceptor(authInterceptor) // Tambahkan AuthInterceptor di sini
         .build()
   }

   @Provides
   @Singleton
   fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
      return Retrofit.Builder()
         .baseUrl(BASE_URL_OFFLINE)
         .client(okHttpClient)
         .addConverterFactory(GsonConverterFactory.create())
         .build()
   }

   @Provides
   @Singleton
   fun provideApiService(retrofit: Retrofit): ApiService {
      return retrofit.create(ApiService::class.java)
   }

   @Provides
   @Singleton
   fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
      return SessionManager(context)
   }
}