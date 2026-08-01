package com.example.freese.utils

import com.example.freese.data.local.pref.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
   private val sessionManager: SessionManager
) : Interceptor {

   override fun intercept(chain: Interceptor.Chain): Response {
      val token = sessionManager.fetchAuthToken()

      val request = if (token != null) {
         chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
      } else {
         chain.request()
      }

      return chain.proceed(request)
   }
}