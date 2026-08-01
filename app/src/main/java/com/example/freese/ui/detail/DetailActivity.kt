package com.example.freese.ui.detail

import ProductResponse
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.freese.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide
import com.example.freese.R
import com.example.freese.viewmodel.CartViewModel
import com.example.freese.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

   private lateinit var binding: ActivityDetailBinding
   private val cartViewModel: CartViewModel by viewModels()
   private var quantity = 1 // Kuantitas default
   private var maxStock = 0 // Akan diisi dari data API

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityDetailBinding.inflate(layoutInflater)
      setContentView(binding.root)

      // 1. Tangkap data dari Intent
      val product = intent.getParcelableExtra<ProductResponse>("EXTRA_PRODUCT")

      if (product != null) {
         setupUI(product)
         setupAction(product)
         setupObservers()
      } else {
         Toast.makeText(this, "Data produk tidak ditemukan", Toast.LENGTH_SHORT).show()
         finish()
      }
   }

   private fun setupUI(product: ProductResponse) {
      maxStock = product.stock

      binding.apply {
         tvDetailTitle.text = product.name
         tvDetailDesc.text = product.description

         val localeID = Locale("in", "ID")
         val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
         formatRupiah.maximumFractionDigits = 0
         tvDetailPrice.text = formatRupiah.format(product.price)

         tvStok.text = "Stok: ${product.stock}"
         tvCategory.text = product.category

         val sellerName = product.owner?.farmName ?: product.owner?.fullName ?: "Penjual Tidak Diketahui"
         tvSellerName.text = sellerName

         tvDomi.text = "Tersedia"

         Glide.with(this@DetailActivity)
            .load(product.image)
            .placeholder(R.drawable.ic_logo_text)
            .into(detailImage)

         val tvQuantity = linearLayout2.getChildAt(1) as android.widget.TextView
         tvQuantity.text = quantity.toString()
      }
   }

   private fun setupAction(product: ProductResponse) {
      binding.ivBack.setOnClickListener {
         onBackPressedDispatcher.onBackPressed()
      }

      val btnMinus = binding.linearLayout2.getChildAt(0)
      val tvQuantity = binding.linearLayout2.getChildAt(1) as android.widget.TextView
      val btnPlus = binding.linearLayout2.getChildAt(2)

      btnMinus.setOnClickListener {
         if (quantity > 1) {
            quantity--
            tvQuantity.text = quantity.toString()
         }
      }

      btnPlus.setOnClickListener {
         if (quantity < maxStock) {
            quantity++
            tvQuantity.text = quantity.toString()
         } else {
            Toast.makeText(this, "Maksimal pembelian adalah stok yang tersedia", Toast.LENGTH_SHORT).show()
         }
      }

      binding.btnWa.text = "Masukkan ke Keranjang"
      binding.btnWa.setOnClickListener {
         // Panggil fungsi addToCart dari ViewModel
         cartViewModel.addToCart(product.id, quantity)
      }
   }
   private fun setupObservers() {
      lifecycleScope.launch {
         cartViewModel.addToCartState.collect { result ->
            when (result) {
               is Result.Loading -> {
                  // Kunci tombol agar tidak dispam
                  binding.btnWa.isEnabled = false
                  binding.btnWa.text = "Memproses..."
               }
               is Result.Success -> {
                  binding.btnWa.isEnabled = true
                  binding.btnWa.text = "Masukkan ke Keranjang"

                  // Tampilkan pesan dari backend
                  Toast.makeText(this@DetailActivity, result.data.msg, Toast.LENGTH_SHORT).show()

                  // Reset state agar aman
                  cartViewModel.resetState()

                  // Opsional: Tutup halaman detail setelah sukses masuk keranjang
                  finish()
               }
               is Result.Error -> {
                  binding.btnWa.isEnabled = true
                  binding.btnWa.text = "Masukkan ke Keranjang"
                  Toast.makeText(this@DetailActivity, result.message, Toast.LENGTH_LONG).show()
                  cartViewModel.resetState()
               }
               null -> { /* State awal, diamkan saja */ }
            }
         }
      }
   }
}