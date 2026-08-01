package com.example.freese.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freese.data.remote.api.response.CartCheckoutRequest
import com.example.freese.data.remote.api.response.CheckoutResponse
import com.example.freese.data.remote.api.response.DirectBuyRequest
import com.example.freese.data.remote.api.response.HistoryResponse
import com.example.freese.data.remote.api.response.IncomingOrderResponse
import com.example.freese.data.remote.api.response.PaymentUrlResponse
import com.example.freese.data.remote.api.response.TransactionActionResponse
import com.example.freese.data.remote.api.response.UpdateStatusResponse
import com.example.freese.repository.TransactionRepository
import com.example.freese.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
   private val repository: TransactionRepository
) : ViewModel() {

   private val _checkoutState = MutableStateFlow<Result<CheckoutResponse>?>(null)
   val checkoutState: StateFlow<Result<CheckoutResponse>?> = _checkoutState.asStateFlow()
   private val _historyState = MutableStateFlow<Result<List<HistoryResponse>>?>(null)
   val historyState: StateFlow<Result<List<HistoryResponse>>?> = _historyState.asStateFlow()

   private val _actionState = MutableStateFlow<Result<TransactionActionResponse>?>(null)
   val actionState: StateFlow<Result<TransactionActionResponse>?> = _actionState.asStateFlow()

   private val _updateStatusState = MutableStateFlow<Result<UpdateStatusResponse>?>(null)
   val updateStatusState: StateFlow<Result<UpdateStatusResponse>?> = _updateStatusState.asStateFlow()

   private val _incomingOrdersState = MutableStateFlow<Result<List<IncomingOrderResponse>>?>(null)
   val incomingOrdersState: StateFlow<Result<List<IncomingOrderResponse>>?> = _incomingOrdersState.asStateFlow()

   private val _paymentUrlState = MutableStateFlow<Result<PaymentUrlResponse>?>(null)
   val paymentUrlState: StateFlow<Result<PaymentUrlResponse>?> = _paymentUrlState.asStateFlow()

   fun getPaymentUrl(transactionId: Int) {
      viewModelScope.launch {
         repository.getPaymentUrl(transactionId).collect { result ->
            _paymentUrlState.value = result
         }
      }
   }

   fun resetPaymentUrlState() {
      _paymentUrlState.value = null
   }
   fun getIncomingOrders() {
      viewModelScope.launch {
         repository.getIncomingOrders().collect { result ->
            _incomingOrdersState.value = result
         }
      }
   }

   fun updateTransactionStatus(transactionId: RequestBody, invoice: MultipartBody.Part) {
      viewModelScope.launch {
         repository.updateTransactionStatus(transactionId, invoice).collect { result ->
            _updateStatusState.value = result
         }
      }
   }

   fun getHistory() {
      viewModelScope.launch {
         repository.getHistory().collect { result ->
            _historyState.value = result
         }
      }
   }

   fun cancelTransaction(id: Int) {
      viewModelScope.launch {
         repository.cancelTransaction(id).collect { result ->
            _actionState.value = result
         }
      }
   }

   fun completeTransaction(id: Int) {
      viewModelScope.launch {
         repository.completeTransaction(id).collect { result ->
            _actionState.value = result
         }
      }
   }

   fun resetActionState() {
      _actionState.value = null
   }

   fun checkoutCart(shipAddress: String) {
      viewModelScope.launch {
         // Buat objek request yang berisi alamat
         val request = CartCheckoutRequest(shipAddress)

         // Kirim request tersebut ke repository
         repository.checkoutCart(request).collect { result ->
            _checkoutState.value = result
         }
      }
   }

   fun directBuy(productId: Int, quantity: Int, shipAddress: String) {
      viewModelScope.launch {
         // Buat objek request yang berisi id, qty, dan alamat
         val request = DirectBuyRequest(productId, quantity, shipAddress)

         // Kirim request tersebut ke repository
         repository.directBuy(request).collect { result ->
            _checkoutState.value = result // Kita bisa pakai state yang sama
         }
      }
   }

   // Fungsi reset agar pop-up Midtrans tidak muncul berkali-kali jika layar diputar
   fun resetCheckoutState() {
      _checkoutState.value = null
   }
}