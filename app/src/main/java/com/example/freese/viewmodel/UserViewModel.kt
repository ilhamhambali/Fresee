package com.example.freese.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freese.data.remote.api.response.ChangePasswordRequest
import com.example.freese.data.remote.api.response.EditProfileResponse
import com.example.freese.data.remote.api.response.TransactionActionResponse
import com.example.freese.data.remote.api.response.UserProfileResponse
import com.example.freese.repository.UserRepository
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
class UserViewModel @Inject constructor(
   private val repository: UserRepository
) : ViewModel() {

   private val _profileState = MutableStateFlow<Result<UserProfileResponse>?>(null)
   val profileState: StateFlow<Result<UserProfileResponse>?> = _profileState.asStateFlow()

   private val _editProfileState = MutableStateFlow<Result<EditProfileResponse>?>(null)
   val editProfileState: StateFlow<Result<EditProfileResponse>?> = _editProfileState.asStateFlow()

   private val _updateProfileState = MutableStateFlow<Result<EditProfileResponse>?>(null)
   val updateProfileState: StateFlow<Result<EditProfileResponse>?> = _updateProfileState.asStateFlow()

   private val _changePasswordState = MutableStateFlow<Result<TransactionActionResponse>?>(null)
   val changePasswordState: StateFlow<Result<TransactionActionResponse>?> = _changePasswordState.asStateFlow()

   fun updateProfile(
      fullName: RequestBody,
      phone: RequestBody,
      address: RequestBody,
      avatar: MultipartBody.Part?
   ) {
      viewModelScope.launch {
         repository.editProfile(fullName, phone, address, avatar).collect { result ->
            _updateProfileState.value = result
         }
      }
   }

   fun changePassword(oldPass: String, newPass: String) {
      viewModelScope.launch {
         repository.changePassword(ChangePasswordRequest(oldPass, newPass)).collect { result ->
            _changePasswordState.value = result
         }
      }
   }

   fun resetActionStates() {
      _updateProfileState.value = null
      _changePasswordState.value = null
   }
   fun getProfile() {
      viewModelScope.launch {
         repository.getProfile().collect { result ->
            _profileState.value = result
         }
      }
   }

   fun saveUserRole(role: String) {
      repository.saveUserRole(role)
   }
}