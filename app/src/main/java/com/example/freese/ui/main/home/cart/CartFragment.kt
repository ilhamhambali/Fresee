package com.example.freese.ui.main.home.cart

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freese.data.remote.api.response.CartItemResponse
import com.example.freese.databinding.FragmentCartBinding
import com.example.freese.utils.Result
import com.example.freese.viewmodel.CartViewModel
import com.example.freese.viewmodel.TransactionViewModel
import com.midtrans.sdk.corekit.core.MidtransSDK
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class CartFragment : Fragment() {
   private var _binding: FragmentCartBinding? = null
   private val binding get() = _binding!!
   private val viewModel: CartViewModel by viewModels()
   private val transactionViewModel: TransactionViewModel by viewModels()
   private lateinit var cartAdapter: CartAdapter
   private var currentCartList: List<CartItemResponse> = emptyList()

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentCartBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      setupRecyclerView()
      setupObservers()

      // Panggil API untuk mengambil data keranjang
      viewModel.getCart()

      // Aksi tombol Checkout
      binding.btnCheckout.setOnClickListener {
         val selectedItems = currentCartList.filter { it.isSelected }
         if (selectedItems.isEmpty()) {
            Toast.makeText(requireContext(), "Pilih minimal 1 barang untuk checkout", Toast.LENGTH_SHORT).show()
         } else {
            // Minta input alamat dengan Pop-Up Dialog sederhana
            val inputAddress = android.widget.EditText(requireContext())
            inputAddress.hint = "Contoh: Jl. Merdeka No 10, RT/RW..."

            AlertDialog.Builder(requireContext())
               .setTitle("Alamat Pengiriman")
               .setMessage("Masukkan alamat pengiriman Anda:")
               .setView(inputAddress)
               .setPositiveButton("Buat Pesanan") { dialog, _ ->
                  val address = inputAddress.text.toString()
                  if (address.isNotEmpty()) {
                     transactionViewModel.checkoutCart(address)
                  } else {
                     Toast.makeText(requireContext(), "Alamat tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                  }
                  dialog.dismiss()
               }
               .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
               .show()
         }
      }
   }

   private fun setupRecyclerView() {
      cartAdapter = CartAdapter (
         onCheckedChange = { clickedItem, isChecked ->
            viewModel.selectCartItem(clickedItem.id, isChecked)
         },
         onQuantityChange = { clickedItem, newQuantity ->
            viewModel.updateCartQuantity(clickedItem.id, newQuantity)
         },
         onDeleteClick = { clickedItem ->
            viewModel.deleteCartItem(clickedItem.id)
         }
      )

      binding.rvCart.apply {
         layoutManager = LinearLayoutManager(requireContext())
         adapter = cartAdapter
         setHasFixedSize(true)
      }
   }

   private fun setupObservers() {
      lifecycleScope.launch {
         // 1. Observer untuk mengambil list keranjang
         viewModel.cartListState.collect { result ->
            when (result) {
               is Result.Loading -> {
                  binding.progressBar.visibility = View.VISIBLE
               }
               is Result.Success -> {
                  binding.progressBar.visibility = View.GONE
                  val listData = result.data
                  currentCartList = listData

                  if (listData.isEmpty()) {
                     binding.tvEmptyCart.visibility = View.VISIBLE
                     binding.rvCart.visibility = View.GONE
                  } else {
                     binding.tvEmptyCart.visibility = View.GONE
                     binding.rvCart.visibility = View.VISIBLE
                     cartAdapter.submitList(listData)
                  }

                  // Hitung ulang total harga tiap kali data baru masuk
                  calculateTotalPrice(listData)
               }
               is Result.Error -> {
                  binding.progressBar.visibility = View.GONE
                  Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
               }
               null -> {}
            }
         }
      }

      lifecycleScope.launch {
         // 2. Observer saat meng-update Checkbox
         viewModel.selectItemState.collect { result ->
            if (result is Result.Success) {
               // Jika sukses update ke database, ambil (refresh) data keranjang terbaru
               // agar hitungan total dan UI sinkron
               viewModel.getCart()

               // Reset state agar tidak terpanggil berulang kali
               viewModel.resetState()
            } else if (result is Result.Error) {
               Toast.makeText(requireContext(), "Gagal mengupdate item", Toast.LENGTH_SHORT).show()
               viewModel.resetState()
            }
         }
      }

      lifecycleScope.launch {
         transactionViewModel.checkoutState.collect { result ->
            when (result) {
               is Result.Loading -> {
                  binding.btnCheckout.isEnabled = false
                  binding.btnCheckout.text = "Memproses..."
               }
               is Result.Success -> {
                  binding.btnCheckout.isEnabled = true
                  binding.btnCheckout.text = "Checkout"

                  Toast.makeText(requireContext(), "Pesanan berhasil dibuat! Silakan bayar di Riwayat Pesanan.", Toast.LENGTH_LONG).show()

                  // Panggil getCart untuk me-refresh keranjang (menghilangkan barang yang sudah dicheckout)
                  viewModel.getCart()

                  transactionViewModel.resetCheckoutState()
               }
               is Result.Error -> {
                  binding.btnCheckout.isEnabled = true
                  binding.btnCheckout.text = "Checkout"
                  Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                  transactionViewModel.resetCheckoutState()
               }
               null -> {}
            }
         }
      }

      lifecycleScope.launch {
         viewModel.updateItemState.collect { result ->
            if (result is Result.Success) {
               // Jika sukses update/hapus, REFRESH keranjang agar UI langsung berubah!
               viewModel.getCart()
               viewModel.resetUpdateState()
            } else if (result is Result.Error) {
               Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
               viewModel.resetUpdateState()
            }
         }
      }
   }

   // Fungsi canggih untuk menghitung total belanja
   private fun calculateTotalPrice(cartItems: List<CartItemResponse>) {
      var totalPrice = 0

      // Looping semua item, cek apakah isSelected == true
      for (item in cartItems) {
         if (item.isSelected) {
            totalPrice += (item.product.price * item.quantity)
         }
      }

      // Tampilkan format Rupiah ke Layar
      val localeID = Locale("in", "ID")
      val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
      formatRupiah.maximumFractionDigits = 0

      binding.tvTotalPrice.text = formatRupiah.format(totalPrice)
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}