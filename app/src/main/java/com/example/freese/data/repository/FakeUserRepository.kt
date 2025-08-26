package com.example.freese.data.repository

import android.content.Context
import com.example.freese.api.response.AuthResponse
import com.example.freese.api.response.User
import com.example.freese.data.model.UserModel
import com.example.freese.data.pref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import java.io.InputStreamReader

/**
 * Implementasi palsu dari UserRepository untuk pengembangan offline.
 * Membaca data dari file JSON di folder /assets.
 */
class FakeUserRepository(
   private val context: Context,
   private val userPreference: UserPreference // Tetap butuh preference untuk save/get session
) : IAuthRepository {

   private val gson = Gson()

   // Helper untuk membaca file dari assets
   private fun readJsonFromAssets(fileName: String): String {
      return InputStreamReader(context.assets.open(fileName)).use { it.readText() }
   }

   override suspend fun saveSession(user: UserModel) {
      userPreference.saveSession(user)
   }

   override fun getSession(): Flow<UserModel> {
      return userPreference.getSession()
   }

   override suspend fun logout() {
      userPreference.logout()
   }

   override suspend fun register(username: String, email: String, password: String, phoneNumber: String): Result<AuthResponse> {
      delay(1000) // Simulasikan delay network
      // Untuk demo, kita anggap registrasi selalu berhasil
      val jsonString = readJsonFromAssets("register_success.json")
      val response = gson.fromJson(jsonString, AuthResponse::class.java)
      return Result.success(response)
   }

   override suspend fun login(username: String, password: String): Result<AuthResponse> {
      delay(1000) // Simulasikan delay network

      // Logika sederhana untuk login palsu
      return if (username == "user" && password == "password") {
         val jsonString = readJsonFromAssets("login_success.json")
         val response = gson.fromJson(jsonString, AuthResponse::class.java)
         Result.success(response)
      } else {
         val jsonString = readJsonFromAssets("auth_error.json")
         val response = gson.fromJson(jsonString, AuthResponse::class.java)
         Result.failure(Exception(response.message))
      }
   }

   override suspend fun getProfile(): Result<User> {
      // Implementasi palsu untuk getProfile jika diperlukan
      delay(500)
      // Anda bisa membuat profile_success.json jika butuh
      return Result.failure(Exception("Fungsi getProfile belum diimplementasikan di FakeRepository"))
   }
}