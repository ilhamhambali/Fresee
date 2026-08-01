package com.example.freese.data.remote.api.response

import com.google.gson.annotations.SerializedName

// Model untuk mengirim data ke API (Body JSON)
data class AddToCartRequest(
   @SerializedName("productId") val productId: Int,
   @SerializedName("quantity") val quantity: Int
)

// Model untuk menerima jawaban dari API
data class AddToCartResponse(
   @SerializedName("msg") val msg: String
   // Kita abaikan objek 'data' karena biasanya pesan sukses ("msg") sudah cukup
)

// Model untuk isi produk di dalam keranjang
data class ProductInCart(
   @SerializedName("id") val id: Int,
   @SerializedName("name") val name: String,
   @SerializedName("price") val price: Int,
   @SerializedName("image") val image: String,
   @SerializedName("stock") val stock: Int
)

// Model untuk item keranjang itu sendiri
data class CartItemResponse(
   @SerializedName("id") val id: Int,
   @SerializedName("quantity") val quantity: Int,
   @SerializedName("isSelected") val isSelected: Boolean,
   @SerializedName("product") val product: ProductInCart
)

// Model untuk Request Update Checkbox (Memilih/Batal pilih item)
data class SelectCartItemRequest(
   @SerializedName("isSelected") val isSelected: Boolean
)

data class UpdateQuantityRequest(
   @SerializedName("quantity") val quantity: Int
)
