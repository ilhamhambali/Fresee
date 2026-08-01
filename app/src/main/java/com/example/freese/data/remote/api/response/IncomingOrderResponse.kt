package com.example.freese.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class OrderUser(
   @SerializedName("full_name") val fullName: String,
   @SerializedName("address") val address: String
)

data class OrderTransaction(
   @SerializedName("id") val id: Int,
   @SerializedName("status") val status: String, // "PENDING", "PROCESSING", dll
   @SerializedName("paymentStatus") val paymentStatus: String, // "UNPAID", "PAID"
   @SerializedName("createdAt") val createdAt: String,
   @SerializedName("user") val user: OrderUser
)

data class IncomingOrderResponse(
   @SerializedName("id") val id: Int, // ID Item
   @SerializedName("quantity") val quantity: Int,
   @SerializedName("price") val price: Int,
   @SerializedName("product") val product: HistoryProduct,
   @SerializedName("transaction") val transaction: OrderTransaction
)

// (OPSIONAL/BISA DIHAPUS) UpdateStatusRequest tidak lagi dipakai
// karena API baru mensyaratkan form-data (Multipart) untuk kirim status + invoice.

data class UpdateStatusResponse(
   @SerializedName("msg") val msg: String,
   @SerializedName("data") val data: InvoiceData // ResiData ganti nama jadi InvoiceData
)

data class InvoiceData(
   @SerializedName("id") val id: Int,
   @SerializedName("status") val status: String,
   @SerializedName("invoice") val invoice: String? // "resi" diganti jadi "invoice"
)