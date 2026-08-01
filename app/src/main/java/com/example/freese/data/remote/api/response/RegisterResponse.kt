package com.example.freese.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
   @SerializedName("message") val message: String,
   @SerializedName("userId") val userId: Int
)
