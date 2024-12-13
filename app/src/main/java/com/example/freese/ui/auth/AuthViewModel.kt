package com.example.freese.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.freese.data.AuthResponse
import com.example.freese.data.User
import com.example.freese.data.pref.UserModel
import com.example.freese.data.repository.UserRepository
import kotlinx.coroutines.launch


class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

   private val _registerResult = MutableLiveData<Result<AuthResponse>>()
   val registerResult: LiveData<Result<AuthResponse>> = _registerResult

   val loginResult = MutableLiveData<Result<AuthResponse>>()

   private val _profile = MutableLiveData<Result<User>>()
   val profile: LiveData<Result<User>> get() = _profile

   fun fetchProfile() {
      viewModelScope.launch {
         val result = userRepository.getProfile()
         _profile.postValue(result)
      }
   }


   fun register(username: String, email: String, password: String,phoneNumber: String) {
      viewModelScope.launch {
         val result = userRepository.register(username, email, password,phoneNumber)
         _registerResult.value = result
      }
   }

   fun login(username: String, password: String) {

      viewModelScope.launch {
         val result = userRepository.login(username, password)
         loginResult.postValue(result)

         result.onSuccess {
            val user = UserModel(
               email = "",
               token = "",
               isLogin = true
            )
            saveSession(user)
         }
      }
   }

   fun getSession(): LiveData<UserModel> {
      return userRepository.getSession().asLiveData()
   }

   fun saveSession(user: UserModel) {
      viewModelScope.launch {
         userRepository.saveSession(user)
      }
   }

   fun logout() {
      viewModelScope.launch {
         userRepository.logout()
      }
   }
}
