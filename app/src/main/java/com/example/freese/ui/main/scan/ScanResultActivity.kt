package com.example.freese.ui.main.scan

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.freese.data.ScanResponse
import com.example.freese.data.repository.ScanRepository
import com.example.freese.databinding.ActivityScanResultBinding

class ScanResultActivity : AppCompatActivity() {

   private lateinit var binding: ActivityScanResultBinding
   private val scanViewModel: ScanViewModel by viewModels {
      ScanViewModelFactory(ScanRepository())
   }
   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityScanResultBinding.inflate(layoutInflater)
      setContentView(binding.root)

      val result = intent.getStringExtra("SCAN_RESULT")
      Log.d("ScanResultActivity", "Received result: $result")

      if (result != null) {
         binding.predictionResult.text = result
      } else {
         binding.predictionResult.text = "No result received"
      }

      scanViewModel.scanResult.observe(this, Observer { response ->
         if(response != null) {
            Log.d("ScanResultActivity", "Response: ${response.message}")
            binding.predictionResult.text = response.message
         } else {
            Log.d("ScanResultActivity", "No Response")
         }
      })

      // Mengambil URI gambar dari Intent
      val imageUri = intent.getStringExtra("imageUri")
      Log.d("ScanResultActivity", "Response X: $this")

      imageUri?.let {
         val uri = Uri.parse(it)
         binding.predictionImage.setImageURI(uri)
      }
   }
}
