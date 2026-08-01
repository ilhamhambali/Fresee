package com.example.freese.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freese.data.remote.api.request.LoginRequest
import com.example.freese.data.remote.api.request.RegisterRequest
import com.example.freese.data.remote.api.response.LoginResponse
import com.example.freese.data.remote.api.response.RegisterResponse
import com.example.freese.repository.AuthRepository
import com.example.freese.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
   private val repository: AuthRepository
) : ViewModel() {

   private val _loginState = MutableStateFlow<Result<LoginResponse>?>(null)
   val loginState: StateFlow<Result<LoginResponse>?> = _loginState.asStateFlow()

   private val _registerState = MutableStateFlow<Result<RegisterResponse>?>(null)
   val registerState: StateFlow<Result<RegisterResponse>?> = _registerState.asStateFlow()

   fun loginUser(loginRequest: LoginRequest) {
      viewModelScope.launch {
         repository.login(loginRequest).collect { result ->
            _loginState.value = result
         }
      }
   }

   fun registerUser(email: RequestBody,
                    password: RequestBody,
                    fullName: RequestBody,
                    phone: RequestBody,
                    role: RequestBody,
                    photo: MultipartBody.Part?) {
      viewModelScope.launch {
         repository.register(email, password, fullName, phone, role, photo).collect { result ->
            _registerState.value = result
         }
      }
   }

   fun isLoggedIn(): Boolean {
      return repository.isLoggedIn()
   }

   fun logout() {
      repository.logout()
   }
}