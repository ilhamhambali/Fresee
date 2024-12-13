package com.example.freese.ui.main.scan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.freese.data.ScanResponse
import com.example.freese.data.repository.ScanRepository
import com.example.freese.helper.getImageUri
import com.example.freese.databinding.ActivityScanBinding
import com.example.freese.helper.reduceFileImage
import com.example.freese.helper.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class ScanActivity : AppCompatActivity() {
   private lateinit var binding: ActivityScanBinding
   private var currentImageUri: Uri? = null
   private val scanViewModel: ScanViewModel by viewModels {
      ScanViewModelFactory(ScanRepository())
   }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityScanBinding.inflate(layoutInflater)
      setContentView(binding.root)

      scanViewModel.scanResult.observe(this) { response ->
         Log.d("ScanActivity", "Got response in activity: ${response?.message}")
      }

      binding.galleryButton.setOnClickListener {
         startGallery()
      }


      binding.captureButton.setOnClickListener {
         startCamera()
      }
   }





   // Fungsi untuk memilih gambar dari galeri
   private fun startGallery() {
      launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
   }

   private val launcherGallery = registerForActivityResult(
      ActivityResultContracts.PickVisualMedia()
   ) { uri: Uri? ->
      if (uri != null) {
         currentImageUri = uri
         val imageFile = uriToFile(uri, this).reduceFileImage()
         Log.d("Image File", "showImage: ${imageFile.path}")

         scanViewModel.uploadImage(imageFile)

         Handler(Looper.getMainLooper()).postDelayed({
            val message = scanViewModel.scanResult.value?.message
            Log.d("ScanActivity", "Message to send: $message")

            // Kirim via Intent
            val intent = Intent(this, ScanResultActivity::class.java)
            intent.putExtra("SCAN_RESULT", message)
            currentImageUri?.let {
               intent.putExtra("imageUri", it.toString())
            }
            startActivity(intent)
         }, 1000)

      } else {
         Log.d("Photo Picker", "No media selected")
      }
   }





   private fun startCamera() {
      // Menyiapkan file temp untuk menyimpan gambar
      currentImageUri = getImageUri(this)
      currentImageUri?.let { uri ->
         launcherIntentCamera.launch(uri)
      }
   }

   private val launcherIntentCamera = registerForActivityResult(
      ActivityResultContracts.TakePicture()
   ) { isSuccess ->
      if (isSuccess) {
         currentImageUri?.let {
            uploadImage()
         }
      } else {
         // Gagal mengambil gambar
         currentImageUri = null
      }
   }

   private fun uploadImage() {
      currentImageUri?.let { uri ->
         val imageFile = uriToFile(uri, this).reduceFileImage()
         Log.d("Image File", "showImage: ${imageFile.path}")

         scanViewModel.uploadImage(imageFile)
         Handler(Looper.getMainLooper()).postDelayed({
            val message = scanViewModel.scanResult.value?.message
            Log.d("ScanActivity", "Message to send: $message")

            // Kirim via Intent
            val intent = Intent(this, ScanResultActivity::class.java)
            intent.putExtra("SCAN_RESULT", message)
            currentImageUri?.let {
               intent.putExtra("imageUri", it.toString())
            }
            startActivity(intent)
         }, 1000)
      }
      
   }


   // Fungsi untuk meneruskan gambar ke ScanResultActivity
   private fun navigateToScanResultActivity() {
      val intent = Intent(this, ScanResultActivity::class.java)
      currentImageUri?.let {
         intent.putExtra("imageUri", it.toString())
      }
      startActivity(intent)

   }







   companion object {
      private const val TAG = "ScanActivity"
      const val EXTRA_CAMERAX_IMAGE = "CameraX Image"
      const val CAMERAX_RESULT = 200
   }
}
