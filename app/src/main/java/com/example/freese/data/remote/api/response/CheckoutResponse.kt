package com.example.freese.data.remote.api.response

import com.google.gson.annotations.SerializedName

// Objek "data" transaksi (ditambah paymentStatus dan paymentUrl)
data class TransactionData(
   @SerializedName("id") val id: Int,
   @SerializedName("userId") val userId: Int,
   @SerializedName("totalPrice") val totalPrice: Int,
   @SerializedName("status") val status: String, // "PENDING", "PROCESSING", "COMPLETED", "CANCELED"
   @SerializedName("paymentStatus") val paymentStatus: String, // "UNPAID", "PAID", "FAILED"
   @SerializedName("paymentUrl") val paymentUrl: String?, // Bisa null kalau belum diminta
   @SerializedName("createdAt") val createdAt: String
)

// Response Utama Checkout Langsung (Biasanya kembaliannya object/data langsung)
data class CheckoutResponse(
   @SerializedName("msg") val msg: String,
   @SerializedName("data") val data: TransactionData
   // snapToken DIHAPUS karena diganti paymentUrl di step terpisah
)

// Request untuk Direct Buy (Sesuai API baru, butuh shipAddress)
data class DirectBuyRequest(
   @SerializedName("productId") val productId: Int,
   @SerializedName("quantity") val quantity: Int,
   @SerializedName("shipAddress") val shipAddress: String
)

// Request untuk Cart Checkout (Hanya butuh alamat, karena barang diambil dari yang isSelected: true)
data class CartCheckoutRequest(
   @SerializedName("shipAddress") val shipAddress: String
)

// BARU: Response khusus untuk saat memanggil /transaction/product/pay/:id
data class PaymentUrlResponse(
   @SerializedName("message") val message: String,
   @SerializedName("paymentUrl") val paymentUrl: String
)