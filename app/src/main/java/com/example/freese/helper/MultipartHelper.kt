package com.example.freese.helper


import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun createImageMultipart(file: File, paramName: String): MultipartBody.Part {
   val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull()) // Sesuaikan MIME type
   return MultipartBody.Part.createFormData(paramName, file.name, requestFile)
}
