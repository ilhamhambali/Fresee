package com.example.freese.data.repository

import com.example.freese.data.AuthResponse
import com.example.freese.data.User
import com.example.freese.data.pref.UserModel
import com.example.freese.data.pref.UserPreference
import com.example.freese.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(username: String, email: String, password: String,phoneNumber: String): Result<AuthResponse> {
        return try {
            val registerRequest = ApiService.RegisterRequest(username, email, password, phoneNumber)

            val response = apiService.register(registerRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(username: String, password: String): Result<AuthResponse> {
        return try {
            val loginRequest = ApiService.LoginRequest(username, password)
            val response = apiService.login(loginRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProfile(): Result<User> {
        return try {
            val response = apiService.getProfile()
            if (response.user != null) {
                Result.success(response.user)
            } else {
                Result.failure(Exception(response.message ?: "User data not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(userPreference, apiService)
        }.also { instance = it }
    }
}
