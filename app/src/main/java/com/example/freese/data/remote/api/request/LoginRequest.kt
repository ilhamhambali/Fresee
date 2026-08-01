package com.example.freese.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
   @SerializedName("email") val email: String,
   @SerializedName("password") val password: String
)