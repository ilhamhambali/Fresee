package com.example.freese.ui.sellermain.sellerproduct

import ProductResponse
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.freese.databinding.ActivityAddEditProductBinding
import com.example.freese.viewmodel.ProductViewModel
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
class AddEditProductActivity : AppCompatActivity() {

   private lateinit var binding: ActivityAddEditProductBinding
   private val viewModel: ProductViewModel by viewModels()

   private var selectedImageUri: Uri? = null
   private var isEditMode = false
   private var productIdToEdit = -1

   // Peluncur untuk membuka Galeri HP
   private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
      if (uri != null) {
         selectedImageUri = uri
         binding.ivProductImage.setImageURI(uri) // Tampilkan gambar yang dipilih
      }
   }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityAddEditProductBinding.inflate(layoutInflater)
      setContentView(binding.root)

      // Cek apakah halaman ini dibuka untuk EDIT
      val productData = intent.getParcelableExtra<ProductResponse>("EXTRA_PRODUCT")
      if (productData != null) {
         isEditMode = true
         productIdToEdit = productData.id
         setupEditMode(productData)
      }

      binding.cvImage.setOnClickListener {
         // Buka galeri untuk pilih gambar (format image/*)
         pickImageLauncher.launch("image/*")
      }

      binding.btnSave.setOnClickListener {
         validateAndSave()
      }

      setupObservers()
   }

   private fun setupEditMode(product: ProductResponse) {
      binding.tvTitle.text = "Edit Produk"
      binding.etName.setText(product.name)
      binding.etCategory.setText(product.category)
      binding.etPrice.setText(product.price.toString())
      binding.etStock.setText(product.stock.toString())
      binding.etDescription.setText(product.description)

      Glide.with(this).load(product.image).into(binding.ivProductImage)
   }

   private fun validateAndSave() {
      val name = binding.etName.text.toString()
      val category = binding.etCategory.text.toString()
      val price = binding.etPrice.text.toString()
      val stock = binding.etStock.text.toString()
      val desc = binding.etDescription.text.toString()

      if (name.isEmpty() || price.isEmpty() || stock.isEmpty()) {
         Toast.makeText(this, "Nama, Harga, dan Stok wajib diisi", Toast.LENGTH_SHORT).show()
         return
      }

      if (!isEditMode && selectedImageUri == null) {
         Toast.makeText(this, "Silakan pilih foto produk", Toast.LENGTH_SHORT).show()
         return
      }

      // Siapkan RequestBody untuk Text
      val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
      val catBody = category.toRequestBody("text/plain".toMediaTypeOrNull())
      val priceBody = price.toRequestBody("text/plain".toMediaTypeOrNull())
      val stockBody = stock.toRequestBody("text/plain".toMediaTypeOrNull())
      val descBody = desc.toRequestBody("text/plain".toMediaTypeOrNull())

      // TAMBAHAN: Buat unitBody (Bisa diisi "kg", "pcs", atau "ikat" sesuai kebutuhan sementara)
      val unitBody = "pcs".toRequestBody("text/plain".toMediaTypeOrNull())

      // Siapkan File Gambar (jika ada)
      var imagePart: MultipartBody.Part? = null
      if (selectedImageUri != null) {
         val file = getFileFromUri(selectedImageUri!!)
         val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
         imagePart = MultipartBody.Part.createFormData("image", file.name, reqFile)
      }

      // Tembak API
      if (isEditMode) {
         viewModel.updateProduct(productIdToEdit, nameBody, priceBody, stockBody, unitBody, catBody, descBody, imagePart)
      } else {
         viewModel.addProduct(nameBody, priceBody, stockBody, unitBody, catBody, descBody, imagePart!!)
      }
   }

   // Fungsi bantuan untuk mengubah URI dari galeri menjadi File fisik
   private fun getFileFromUri(uri: Uri): File {
      val inputStream = contentResolver.openInputStream(uri)
      val tempFile = File.createTempFile("upload", ".jpg", cacheDir)
      val outputStream = FileOutputStream(tempFile)
      inputStream?.copyTo(outputStream)
      inputStream?.close()
      outputStream.close()
      return tempFile
   }

   private fun setupObservers() {
      // Pantau hasil Add atau Update dari ViewModel
      lifecycleScope.launch {
         viewModel.addProductState.collect { result ->
            when (result) {
               is Result.Loading -> {
                  binding.progressBar.visibility = View.VISIBLE
                  binding.btnSave.isEnabled = false
               }
               is Result.Success -> {
                  binding.progressBar.visibility = View.GONE
                  Toast.makeText(this@AddEditProductActivity, "Berhasil disimpan!", Toast.LENGTH_SHORT).show()
                  finish() // Tutup halaman form dan kembali ke daftar produk
               }
               is Result.Error -> {
                  binding.progressBar.visibility = View.GONE
                  binding.btnSave.isEnabled = true
                  Toast.makeText(this@AddEditProductActivity, result.message, Toast.LENGTH_SHORT).show()
               }
               null -> {}
            }
         }
      }
   }
}