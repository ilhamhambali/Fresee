package com.example.freese.ui.main.option.account

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.freese.R
import com.example.freese.data.remote.api.response.UserProfileResponse // Sesuaikan dengan nama model Anda
import com.example.freese.databinding.ActivityAccountBinding
import com.example.freese.viewmodel.UserViewModel
import com.example.freese.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class AccountActivity : AppCompatActivity() {

   private lateinit var binding: ActivityAccountBinding
   private val viewModel: UserViewModel by viewModels()

   private var selectedImageUri: Uri? = null

   // Peluncur Galeri untuk pilih foto profil baru
   private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
      if (uri != null) {
         selectedImageUri = uri
         binding.ivAvatar.setImageURI(uri) // Tampilkan foto yang baru dipilih
      }
   }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityAccountBinding.inflate(layoutInflater)
      setContentView(binding.root)

      // 1. Ambil data profil dari Intent (dikirim dari AccountFragment)
      val currentProfile = intent.getParcelableExtra<UserProfileResponse>("EXTRA_PROFILE")

      // 2. Isi form dengan data yang sudah ada
      if (currentProfile != null) {
         populateData(currentProfile)
      } else {
         Toast.makeText(this, "Gagal memuat data profil", Toast.LENGTH_SHORT).show()
         finish()
      }

      // 3. Aksi Tombol & Klik
      binding.btnBack.setOnClickListener {
         onBackPressedDispatcher.onBackPressed()
      }

      binding.flAvatarContainer.setOnClickListener {
         pickImageLauncher.launch("image/*") // Buka galeri
      }

      binding.btnSave.setOnClickListener {
         validateAndSave()
      }

      setupObservers()
   }

   private fun populateData(profile: UserProfileResponse) {
      binding.etFullName.setText(profile.fullName)
      binding.etPhone.setText(profile.phone ?: "")
      binding.etAddress.setText(profile.address ?: "")

      // Sembunyikan field Nama Kebun karena backend tidak menyediakan field khusus ini lagi
      // (Atau abaikan jika Anda akan memasukkannya ke kolom deskripsi/alamat)
      binding.tilFarmName.visibility = View.GONE

      // Tampilkan foto avatar (Ganti avatar & image menjadi photo)
      if (profile.photo != null) {
         Glide.with(this)
            .load(profile.photo) // Langsung pakai URL karena backend sudah pakai Google Cloud
            .placeholder(R.drawable.ic_profile)
            .into(binding.ivAvatar)
      }
   }

   private fun validateAndSave() {
      val fullName = binding.etFullName.text.toString().trim()
      val phone = binding.etPhone.text.toString().trim()
      val address = binding.etAddress.text.toString().trim()

      if (fullName.isEmpty()) {
         Toast.makeText(this, "Nama Lengkap wajib diisi", Toast.LENGTH_SHORT).show()
         return
      }

      val nameBody = fullName.toRequestBody("text/plain".toMediaTypeOrNull())
      val phoneBody = phone.toRequestBody("text/plain".toMediaTypeOrNull())
      val addressBody = address.toRequestBody("text/plain".toMediaTypeOrNull())

      var imagePart: MultipartBody.Part? = null
      if (selectedImageUri != null) {
         val file = getFileFromUri(selectedImageUri!!)
         val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
         imagePart = MultipartBody.Part.createFormData("photo", file.name, reqFile) // Ganti 'avatar' jadi 'photo'
      }

      // Hapus farmBody dari pemanggilan ini sesuai repository baru
      viewModel.updateProfile(nameBody, phoneBody, addressBody, imagePart)
   }

   // Fungsi bantuan untuk mengubah URI gambar dari galeri menjadi File fisik
   private fun getFileFromUri(uri: Uri): File {
      val inputStream = contentResolver.openInputStream(uri)
      val tempFile = File.createTempFile("avatar_upload", ".jpg", cacheDir)
      val outputStream = FileOutputStream(tempFile)
      inputStream?.copyTo(outputStream)
      inputStream?.close()
      outputStream.close()
      return tempFile
   }

   private fun setupObservers() {
      lifecycleScope.launch {
         viewModel.updateProfileState.collect { result ->
            when (result) {
               is Result.Loading -> {
                  binding.progressBar.visibility = View.VISIBLE
                  binding.btnSave.isEnabled = false
               }
               is Result.Success -> {
                  binding.progressBar.visibility = View.GONE
                  Toast.makeText(this@AccountActivity, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                  viewModel.resetActionStates() // Reset state
                  finish() // Tutup halaman edit, kembali ke AccountFragment
               }
               is Result.Error -> {
                  binding.progressBar.visibility = View.GONE
                  binding.btnSave.isEnabled = true
                  Toast.makeText(this@AccountActivity, result.message, Toast.LENGTH_LONG).show()
                  viewModel.resetActionStates()
               }
               null -> {}
            }
         }
      }
   }
}