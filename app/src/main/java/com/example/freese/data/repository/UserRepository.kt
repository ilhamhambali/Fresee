package com.example.freese.data.repository

import com.example.freese.api.response.AuthResponse
import com.example.freese.api.response.User
import com.example.freese.data.model.UserModel
import com.example.freese.data.pref.UserPreference
import com.example.freese.api.ApiService
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) : IAuthRepository {

    override suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    override fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    override suspend fun logout() {
        userPreference.logout()
    }

    override suspend fun register(username: String, email: String, password: String,phoneNumber: String): Result<AuthResponse> {
        return try {
            val registerRequest = ApiService.RegisterRequest(username, email, password, phoneNumber)

            val response = apiService.register(registerRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(username: String, password: String): Result<AuthResponse> {
        return try {
            val loginRequest = ApiService.LoginRequest(username, password)
            val response = apiService.login(loginRequest)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProfile(): Result<User> {
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
