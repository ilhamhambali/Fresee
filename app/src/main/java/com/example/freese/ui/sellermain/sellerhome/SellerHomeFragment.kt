package com.example.freese.ui.sellermain.sellerhome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.freese.databinding.FragmentSellerHomeBinding
import com.example.freese.viewmodel.ProductViewModel
import com.example.freese.viewmodel.TransactionViewModel
import com.example.freese.viewmodel.UserViewModel
import com.example.freese.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import kotlin.getValue

@AndroidEntryPoint
class SellerHomeFragment : Fragment() {

   private var _binding: FragmentSellerHomeBinding? = null
   private val binding get() = _binding!!

   // Suntikkan ketiga ViewModel yang kita butuhkan
   private val userViewModel: UserViewModel by viewModels()
   private val productViewModel: ProductViewModel by viewModels()
   private val transactionViewModel: TransactionViewModel by viewModels()

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
      _binding = FragmentSellerHomeBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      setupObservers()
   }

   // Gunakan onResume agar statistik langsung update jika user pindah tab lalu kembali
   override fun onResume() {
      super.onResume()
      // Panggil ketiga API sekaligus!
      userViewModel.getProfile()
      productViewModel.getMyProducts()
      transactionViewModel.getIncomingOrders()
   }

   private fun setupObservers() {
      // 1. Observer Profil
      lifecycleScope.launch {
         userViewModel.profileState.collect { result ->
            if (result is Result.Success) {
               val user = result.data
               binding.tvGreeting.text = "Halo, ${user.fullName}!"

               // Ganti farmName dengan address (atau kosongi jika tidak ada)
               binding.tvFarmName.text = user.address ?: "Toko Belum Dinamai"

               // Ganti avatar menjadi photo, dan HAPUS IP LOCALHOST
               if (user.photo != null) {
                  Glide.with(requireContext()).load(user.photo).into(binding.ivSellerAvatar)
               }
            }
         }
      }

      // 2. Observer Produk (Hitung jumlah produk)
      lifecycleScope.launch {
         productViewModel.myProductsState.collect { result ->
            if (result is Result.Success) {
               binding.tvTotalProducts.text = result.data.size.toString()
            }
         }
      }

      // 3. Observer Pesanan (Hitung Pendapatan & Pesanan Tertunda)
      lifecycleScope.launch {
         transactionViewModel.incomingOrdersState.collect { result ->
            when (result) {
               is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
               is Result.Success -> {
                  binding.progressBar.visibility = View.GONE
                  val orders = result.data

                  // Hitung jumlah pesanan yang statusnya masih "PAID" (Harus segera dikirim)
                  val pendingOrdersCount = orders.count { it.transaction.status == "PAID" }
                  binding.tvPendingOrders.text = pendingOrdersCount.toString()

                  // Hitung total pendapatan (Gabungan dari pesanan PAID, SENT, dan COMPLETED)
                  // Kita kalikan harga * kuantitas
                  var totalRevenue = 0
                  for (order in orders) {
                     if (order.transaction.status == "PAID" ||
                        order.transaction.status == "PROCESSING" || // <--- Ubah di sini
                        order.transaction.status == "COMPLETED") {
                        totalRevenue += (order.price * order.quantity)
                     }
                  }

                  // Format ke Rupiah
                  val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
                  formatRupiah.maximumFractionDigits = 0
                  binding.tvTotalRevenue.text = formatRupiah.format(totalRevenue)
               }
               is Result.Error -> {
                  binding.progressBar.visibility = View.GONE
                  Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
               }
               null -> {}
            }
         }
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}