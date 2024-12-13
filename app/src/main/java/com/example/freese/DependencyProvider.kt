package com.example.freese

import android.content.Context
import com.example.freese.data.pref.UserPreference
import com.example.freese.data.pref.dataStore
import com.example.freese.data.repository.ProductRepository
import com.example.freese.data.repository.ScanRepository
import com.example.freese.data.repository.UserRepository
import com.example.freese.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import com.example.freese.retrofit.ApiConfig
import com.example.freese.retrofit.ScanApiConfig

object DependencyProvider {
    private var userRepository: UserRepository? = null
    private var productRepository: ProductRepository? = null
    private var apiService: ApiService? = null
    private var userPreference: UserPreference? = null

    fun provideUserRepository(context: Context): UserRepository {
        if (userRepository == null) {
            userRepository = UserRepository.getInstance(
                provideUserPreference(context),
                provideApiService(context)
            )
        }
        return userRepository!!
    }
//    fun provideScanRepository(context: Context): ScanRepository {
//        if (userRepository == null) {
//            userRepository = UserRepository.getInstance(
//                provideScanApiService(context)
//            )
//        }
//        return userRepository!!
//    }
//    fun provideProductRepository(context: Context): UserRepository {
//        if (productRepository == null) {
//            productRepository = ProductRepository.getInstance(
//                provideApiService(context)
//            )
//        }
//        return userRepository!!
//    }

    private fun provideApiService(context: Context): ApiService {
        if (apiService == null) {
            val token = runBlocking {
                provideUserPreference(context).getSession().first().token
            }
            apiService = ApiConfig.getApiService(token)
        }
        return apiService!!
    }

    private fun provideScanApiService(context: Context): ApiService {
        if (apiService == null) {
            apiService = ScanApiConfig.getApiService()
        }
        return apiService!!
    }

    private fun provideUserPreference(context: Context): UserPreference {
        if (userPreference == null) {
            userPreference = UserPreference.getInstance(context.dataStore)
        }
        return userPreference!!
        }
}