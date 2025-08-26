package com.example.freese.data.repository

import com.example.freese.api.response.AuthResponse
import com.example.freese.api.response.User
import com.example.freese.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
   suspend fun saveSession(user: UserModel)
   fun getSession(): Flow<UserModel>
   suspend fun logout()
   suspend fun register(username: String, email: String, password: String, phoneNumber: String): Result<AuthResponse>
   suspend fun login(username: String, password: String): Result<AuthResponse>
   suspend fun getProfile(): Result<User>
}