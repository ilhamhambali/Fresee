package com.example.freese.data.remote.api

import AddProductResponse
import ProductResponse
import com.example.freese.data.remote.api.request.LoginRequest
import com.example.freese.data.remote.api.response.LoginResponse
import com.example.freese.data.remote.api.request.RegisterRequest
import com.example.freese.data.remote.api.response.AddToCartRequest
import com.example.freese.data.remote.api.response.AddToCartResponse
import com.example.freese.data.remote.api.response.CartItemResponse
import com.example.freese.data.remote.api.response.ChangePasswordRequest
import com.example.freese.data.remote.api.response.CheckoutResponse
import com.example.freese.data.remote.api.response.DirectBuyRequest
import com.example.freese.data.remote.api.response.EditProfileResponse
import com.example.freese.data.remote.api.response.HistoryResponse
import com.example.freese.data.remote.api.response.IncomingOrderResponse
import com.example.freese.data.remote.api.response.RegisterResponse
import com.example.freese.data.remote.api.response.ScanResponse
import com.example.freese.data.remote.api.response.SelectCartItemRequest
import com.example.freese.data.remote.api.response.TransactionActionResponse
import com.example.freese.data.remote.api.response.UpdateQuantityRequest
import com.example.freese.data.remote.api.response.UpdateStatusResponse
import com.example.freese.data.remote.api.response.UserProfileResponse
import com.example.freese.data.remote.model.InventarisBuah
import com.example.freese.data.remote.model.TambahBuahRequest

// (Tambahan Import untuk Model Baru yang kita bahas sebelumnya)
import com.example.freese.data.remote.api.response.PaymentUrlResponse
import com.example.freese.data.remote.api.response.CartCheckoutRequest

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

   //                               --- TAHAP 1: AUTH ---

   // Register sekarang pakai Multipart karena bisa upload foto (photo) opsional
   @Multipart
   @POST("auth/register")
   suspend fun registerUser(
      @Part("email") email: RequestBody,
      @Part("password") password: RequestBody,
      @Part("full_name") fullName: RequestBody,
      @Part("phone") phone: RequestBody,
      @Part("role") role: RequestBody, // "FARMER" atau "BUYER"
      @Part photo: MultipartBody.Part? // Opsional
   ): Response<RegisterResponse>

   @POST("auth/login")
   suspend fun loginUser(
      @Body request: LoginRequest
   ): Response<LoginResponse>


   //                              --- TAHAP 2: PROFILE ---

   @GET("profile")
   suspend fun getProfile(): Response<UserProfileResponse>

   @Multipart
   @PUT("profile/edit/data")
   suspend fun editProfile(
      @Part("full_name") fullName: RequestBody,
      @Part("phone") phone: RequestBody,
      @Part("address") address: RequestBody,
      @Part photo: MultipartBody.Part? // avatar diganti jadi photo
   ): Response<EditProfileResponse>

   // Ganti Password (Rutenya berubah)
   @PUT("profile/edit/akun")
   suspend fun changePassword(
      @Body request: ChangePasswordRequest
   ): Response<TransactionActionResponse>


   //                              --- TAHAP 3: PRODUCTS ---

   // Dapatkan Semua Produk (Dengan filter tambahan opsional)
   @GET("product")
   suspend fun getAllProducts(
      @Query("search") searchKeyword: String? = null,
      @Query("category") category: String? = null,
      @Query("minPrice") minPrice: Int? = null,
      @Query("maxPrice") maxPrice: Int? = null
   ): Response<List<ProductResponse>>

   // Dapatkan Produk Milik Penjual (FARMER Only)
   @GET("product/myproducts")
   suspend fun getMyProducts(): Response<List<ProductResponse>>

   // Tambah Produk Baru (FARMER Only - Tambah parameter unit)
   @Multipart
   @POST("product")
   suspend fun addProduct(
      @Part("name") name: RequestBody,
      @Part("price") price: RequestBody,
      @Part("stock") stock: RequestBody,
      @Part("unit") unit: RequestBody, // Baru
      @Part("category") category: RequestBody,
      @Part("description") description: RequestBody,
      @Part image: MultipartBody.Part
   ): Response<AddProductResponse>

   // Edit Produk
   @Multipart
   @PUT("product/edit/{id}")
   suspend fun updateProduct(
      @Path("id") id: Int,
      @Part("name") name: RequestBody,
      @Part("price") price: RequestBody,
      @Part("stock") stock: RequestBody,
      @Part("unit") unit: RequestBody, // Baru
      @Part("category") category: RequestBody,
      @Part("description") description: RequestBody,
      @Part image: MultipartBody.Part?
   ): Response<AddProductResponse>

   // Hapus produk
   @DELETE("product/{id}")
   suspend fun deleteProduct(@Path("id") id: Int): Response<TransactionActionResponse>


   //                              --- TAHAP 4: CART ---

   @POST("cart")
   suspend fun addToCart(
      @Body request: AddToCartRequest
   ): Response<AddToCartResponse>

   @GET("cart")
   suspend fun getCart(): Response<List<CartItemResponse>>

   @PATCH("cart/{id}/select")
   suspend fun selectCartItem(
      @Path("id") cartItemId: Int,
      @Body request: SelectCartItemRequest
   ): Response<AddToCartResponse>

   @PUT("cart/{id}")
   suspend fun updateCartQuantity(
      @Path("id") id: Int, @Body req: UpdateQuantityRequest
   ): Response<AddToCartResponse>

   @DELETE("cart/{id}")
   suspend fun deleteCartItem(
      @Path("id") id: Int
   ): Response<AddToCartResponse>


   //                              --- TAHAP 5: TRANSACTION ---

   // Checkout keranjang yang isSelected: true
   @POST("transaction/product/cart")
   suspend fun checkoutCart(
      @Body request: CartCheckoutRequest // Butuh kirim alamat
   ): Response<CheckoutResponse>

   // Beli langsung 1 barang
   @POST("transaction/product")
   suspend fun directBuy(
      @Body request: DirectBuyRequest // Butuh productId, qty, shipAddress
   ): Response<CheckoutResponse>

   // BARU: Request Link Pembayaran Midtrans (BUYER Only)
   @POST("transaction/product/pay/{transactionsId}")
   suspend fun getPaymentUrl(
      @Path("transactionsId") transactionsId: Int
   ): Response<PaymentUrlResponse>

   // Riwayat Pesanan (Bisa memfilter yang PAID)
   @GET("transaction/history")
   suspend fun getTransactionHistory(
      @Query("status") paymentStatus: String? = null
   ): Response<List<HistoryResponse>>

   // Pembeli Mengkonfirmasi Barang Diterima (COMPLETED)
   @PATCH("transaction/product/status/complete")
   suspend fun completeTransaction(
      // Karena body JSON butuh { "transactionId": 1, "status": "COMPLETED" }, kita pakai Map
      @Body request: Map<String, Any>
   ): Response<TransactionActionResponse>

   // Penjual: Lihat Pesanan Masuk (API Doc bilang pakai history juga, tapi kita map response ke IncomingOrderResponse)
   @GET("transaction/history")
   suspend fun getIncomingOrders(): Response<List<IncomingOrderResponse>>

   // Penjual: Upload Resi / Invoice (FARMER Only)
   // Rute berubah menjadi form-data dengan transactionId dan invoice
   @Multipart
   @PATCH("transaction/product/edit/status/")
   suspend fun updateTransactionStatus(
      @Part("transactionId") transactionId: RequestBody,
      @Part invoice: MultipartBody.Part
   ): Response<UpdateStatusResponse>

   // Batalkan Pesanan
   @DELETE("transaction/{id}")
   suspend fun cancelTransaction(@Path("id") id: Int): Response<TransactionActionResponse>


   //                              --- TAHAP 6: DAPUR & SCAN ---
   @GET("api/dapur")
   suspend fun getInventaris(): Response<List<InventarisBuah>>

   @POST("api/dapur")
   suspend fun tambahBuah(@Body request: TambahBuahRequest): Response<InventarisBuah>

   @Multipart
   @POST("/predict")
   suspend fun postImageScan(
      @Part img: MultipartBody.Part
   ): ScanResponse

}