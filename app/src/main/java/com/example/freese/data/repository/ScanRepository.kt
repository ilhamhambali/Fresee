package com.example.freese.data.repository

import com.example.freese.data.ScanResponse
import com.example.freese.retrofit.ScanApiConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ScanRepository {
   private fun createImageMultipart(file: File, paramName: String): MultipartBody.Part {
      val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
      return MultipartBody.Part.createFormData(paramName, file.name, requestFile)
   }

   suspend fun uploadImage(file: File): ScanResponse {
      val apiService = ScanApiConfig.getApiService()
      val imagePart = createImageMultipart(file, "img")
      return apiService.postImageScan(imagePart)
   }
}




//   companion object {
//      @Volatile
//      private var instance: ScanRepository? = null
//      fun getInstance(
//         userPreference: UserPreference,
//         apiService: ApiService
//      ): ScanRepository = instance ?: synchronized(this) {
//         instance ?: ScanRepository(apiService)
//      }.also { instance = it }
//   }

