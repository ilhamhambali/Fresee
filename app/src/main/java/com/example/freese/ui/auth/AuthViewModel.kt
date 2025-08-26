package com.example.freese.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.freese.api.response.AuthResponse
import com.example.freese.api.response.User
import com.example.freese.data.model.UserModel
import com.example.freese.data.repository.IAuthRepository
import kotlinx.coroutines.launch

//@HiltViewModel
class AuthViewModel(private val userRepository: IAuthRepository) : ViewModel() {

   private val _registerResult = MutableLiveData<Result<AuthResponse>>()
   val registerResult: LiveData<Result<AuthResponse>> = _registerResult

   private val _loginResult = MutableLiveData<Result<AuthResponse>>()
   // Ekspos sebagai LiveData yang tidak bisa diubah
   val loginResult: LiveData<Result<AuthResponse>> = _loginResult

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
         _loginResult.postValue(result)

         result.onSuccess { authResponse ->
            // Ambil data dari respons API
            val userModel = UserModel(
               // Asumsi username atau email ada di dalam respons, jika tidak, gunakan yang dari input
               email = username,
               token = authResponse.token?: "", // Ambil token dari respons
               isLogin = true
            )
            saveSession(userModel)
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
