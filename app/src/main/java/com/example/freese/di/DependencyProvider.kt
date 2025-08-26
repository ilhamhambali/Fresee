package com.example.freese.di

import android.content.Context
import com.example.freese.data.pref.UserPreference
import com.example.freese.data.pref.dataStore
import com.example.freese.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.example.freese.api.ApiConfig
import com.example.freese.data.repository.FakeUserRepository
import com.example.freese.data.repository.IAuthRepository

object DependencyProvider {

    private const val USE_FAKE_API = true

    fun provideUserRepository(context: Context): IAuthRepository { // <-- Kembalikan interface
        return if (USE_FAKE_API) {
            FakeUserRepository(
                context.applicationContext,
                provideUserPreference(context)
            )
        } else {
            val userPreference = provideUserPreference(context)
            val token = runBlocking { userPreference.getSession().first().token }
            val apiService = ApiConfig.getApiService(token)
            UserRepository.getInstance(userPreference, apiService)
        }
    }

    private fun provideUserPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context.dataStore)
    }



































//    private var userRepository: UserRepository? = null
//    private var productRepository: ProductRepository? = null
//    private var apiService: ApiService? = null
//    private var userPreference: UserPreference? = null
//
//    fun provideUserRepository(context: Context): UserRepository {
//        if (userRepository == null) {
//            userRepository = UserRepository.getInstance(
//                provideUserPreference(context),
//                provideApiService(context)
//            )
//        }
//        return userRepository!!
//    }
//
//    private fun provideApiService(context: Context): ApiService {
//        if (apiService == null) {
//            val token = runBlocking {
//                provideUserPreference(context).getSession().first().token
//            }
//            apiService = ApiConfig.getApiService(token)
//        }
//        return apiService!!
//    }
//
//
//    private fun provideUserPreference(context: Context): UserPreference {
//        if (userPreference == null) {
//            userPreference = UserPreference.getInstance(context.dataStore)
//        }
//        return userPreference!!
//        }
}