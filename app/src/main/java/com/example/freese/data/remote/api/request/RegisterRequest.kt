package com.example.freese.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
   @SerializedName("email") val email: String,
   @SerializedName("password") val password: String,
   @SerializedName("full_name") val fullName: String, // Sesuaikan dengan API
   @SerializedName("phone") val phone: String,        // Sesuaikan dengan API
   @SerializedName("role") val role: String
)