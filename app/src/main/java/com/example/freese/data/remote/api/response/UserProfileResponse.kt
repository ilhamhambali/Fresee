package com.example.freese.data.remote.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfileResponse(
   @SerializedName("id") val id: Int,
   @SerializedName("email") val email: String,
   @SerializedName("full_name") val fullName: String,
   @SerializedName("phone") val phone: String?,
   @SerializedName("address") val address: String?,
   @SerializedName("role") val role: String, // Wajib: "FARMER" atau "BUYER"
   @SerializedName("photo") val photo: String? // Menyimpan link Google Cloud Storage
): Parcelable

data class EditProfileResponse(
   @SerializedName("msg") val msg: String,
   @SerializedName("data") val data: UserProfileResponse
)

data class ChangePasswordRequest(
   @SerializedName("oldPassword") val oldPassword: String,
   @SerializedName("newPassword") val newPassword: String
)