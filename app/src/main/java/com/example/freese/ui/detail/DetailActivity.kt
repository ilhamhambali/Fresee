package com.example.freese.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.freese.data.ProductModel
import com.example.freese.databinding.ActivityDetailBinding
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.freese.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DetailActivity : AppCompatActivity() {

   private lateinit var binding: ActivityDetailBinding
   private val detailViewModel: DetailViewModel by viewModels()

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityDetailBinding.inflate(layoutInflater)
      setContentView(binding.root)

      val product = intent.getParcelableExtra<ProductModel>("EXTRA_PRODUCT")
      product?.let {
         Glide.with(this)
            .load(it.imageRes)
            .into(binding.detailImage)
         binding.tvDetailPrice.text = it.price
         binding.tvDetailTitle.text = it.name
         binding.tvDetailDesc.text = it.description
         binding.tvStok.text = it.stok
         binding.tvCategory.text = it.category
         binding.tvSellerName.text = it.seller
         binding.tvDomi.text = it.cityName

         // Load favorite status
         detailViewModel.loadFavoriteStatus(it.id)
      }

      // Observasi perubahan status favorit
      detailViewModel.isFavorite.observe(this, Observer { isFavorite ->
         updateFavoriteButton(isFavorite)
      })

      binding.btnFavorite.setOnClickListener {
         product?.let {
            detailViewModel.toggleFavorite(it.id)
            saveToFavorites(it)
         }
      }


      // Tombol kembali
      setSupportActionBar(binding.toolbar)
      supportActionBar?.setDisplayHomeAsUpEnabled(true)
      binding.toolbar.setNavigationOnClickListener {
         onBackPressed()
      }

      binding.btnWa.setOnClickListener {
         val phoneNumber = "6289656647559" // Nomor WhatsApp tujuan
         val message = "Halo, saya tertarik dengan produk Anda!" // Pesan yang ingin dikirim
         openWhatsApp(phoneNumber, message)
      }
   }

   private fun updateFavoriteButton(isFavorite: Boolean) {
      val color = if (isFavorite) {
         R.color.red
      } else {
         R.color.text_primary
      }
      binding.btnFavorite.setColorFilter(
         ContextCompat.getColor(this, color),
         android.graphics.PorterDuff.Mode.SRC_IN
      )
   }

   private fun saveToFavorites(product: ProductModel) {
      // Dapatkan SharedPreferences
      val sharedPref = getSharedPreferences("FAVORITES_PREF", Context.MODE_PRIVATE)
      val editor = sharedPref.edit()

      // Ambil data favorit yang sudah ada
      val gson = Gson()
      val type = object : TypeToken<MutableList<ProductModel>>() {}.type
      val favorites: MutableList<ProductModel> = gson.fromJson(
         sharedPref.getString("FAVORITES_LIST", "[]"), type
      ) ?: mutableListOf()

      // Tambahkan produk baru ke daftar favorit
      if (!favorites.any { it.id == product.id }) { // Cegah duplikasi
         favorites.add(product)
      }

      // Simpan kembali ke SharedPreferences
      editor.putString("FAVORITES_LIST", gson.toJson(favorites))
      editor.apply()
   }

   private fun openWhatsApp(phoneNumber: String, message: String) {
      try {
         val url = "https://wa.me/$phoneNumber?text=${Uri.encode(message)}"
         val intent = Intent(Intent.ACTION_VIEW)
         intent.data = Uri.parse(url)
         intent.setPackage("com.whatsapp")
         startActivity(intent)
      } catch (e: Exception) {
         Toast.makeText(this, "WhatsApp tidak ditemukan di perangkat Anda.", Toast.LENGTH_SHORT).show()
      }
   }

}


