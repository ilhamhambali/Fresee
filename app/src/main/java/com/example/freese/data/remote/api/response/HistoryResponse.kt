package com.example.freese.data.remote.api.response
import com.google.gson.annotations.SerializedName

// Model untuk produk di dalam riwayat
data class HistoryProduct(
   @SerializedName("name") val name: String,
   @SerializedName("image") val image: String
)

// Model untuk detail item yang dibeli
data class HistoryItem(
   @SerializedName("id") val id: Int?,
   @SerializedName("quantity") val quantity: Int,
   @SerializedName("price") val price: Int,
   @SerializedName("product") val product: HistoryProduct
)

// Model utama untuk satu kotak Transaksi
data class HistoryResponse(
   @SerializedName("id") val id: Int,
   @SerializedName("totalPrice") val totalPrice: Int,
   @SerializedName("status") val status: String,
   @SerializedName("createdAt") val createdAt: String,
   @SerializedName("items") val items: List<HistoryItem>
)

// Model sederhana untuk respon sukses Cancel/Complete
data class TransactionActionResponse(
   @SerializedName("msg") val msg: String
)