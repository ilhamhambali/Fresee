package com.example.freese.repository

import com.example.freese.data.remote.api.ApiService
import com.example.freese.data.remote.api.response.CartCheckoutRequest
import com.example.freese.data.remote.api.response.CheckoutResponse
import com.example.freese.data.remote.api.response.DirectBuyRequest
import com.example.freese.data.remote.api.response.HistoryResponse
import com.example.freese.data.remote.api.response.IncomingOrderResponse
import com.example.freese.data.remote.api.response.PaymentUrlResponse
import com.example.freese.data.remote.api.response.TransactionActionResponse
import com.example.freese.data.remote.api.response.UpdateStatusResponse
import com.example.freese.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
   private val apiService: ApiService
) {
   // Fungsi Checkout (Membeli dari keranjang)
   // Checkout keranjang sekarang butuh alamat pengiriman
   fun checkoutCart(request: CartCheckoutRequest): Flow<Result<CheckoutResponse>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.checkoutCart(request)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal Checkout: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   // Fungsi Direct Buy (Membeli langsung 1 barang)
   fun directBuy(request: DirectBuyRequest): Flow<Result<CheckoutResponse>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.directBuy(request)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal Pembelian Langsung: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun getHistory(): Flow<Result<List<HistoryResponse>>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.getTransactionHistory()
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal memuat riwayat: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun cancelTransaction(id: Int): Flow<Result<TransactionActionResponse>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.cancelTransaction(id)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal membatalkan pesanan: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun completeTransaction(transactionId: Int): Flow<Result<TransactionActionResponse>> = flow {
      emit(Result.Loading)
      try {
         // Kita bungkus ID transaksi dan status menjadi Map sesuai permintaan Backend
         val requestBody = mapOf(
            "transactionId" to transactionId,
            "status" to "COMPLETED"
         )
         val response = apiService.completeTransaction(requestBody)

         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal menyelesaikan pesanan: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun getIncomingOrders(): Flow<Result<List<IncomingOrderResponse>>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.getIncomingOrders()
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal memuat pesanan: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun getPaymentUrl(transactionId: Int): Flow<Result<PaymentUrlResponse>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.getPaymentUrl(transactionId)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal memuat link pembayaran: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }

   fun updateTransactionStatus(
      transactionId: RequestBody, // Parameter ini berubah jadi RequestBody
      invoice: MultipartBody.Part // 'resi' berubah nama jadi 'invoice'
   ): Flow<Result<UpdateStatusResponse>> = flow {
      emit(Result.Loading)
      try {
         val response = apiService.updateTransactionStatus(transactionId, invoice)
         if (response.isSuccessful && response.body() != null) {
            emit(Result.Success(response.body()!!))
         } else {
            emit(Result.Error("Gagal update status: ${response.message()}"))
         }
      } catch (e: Exception) {
         emit(Result.Error(e.message ?: "Terjadi kesalahan koneksi"))
      }
   }
}