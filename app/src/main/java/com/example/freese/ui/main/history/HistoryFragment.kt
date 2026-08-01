package com.example.freese.ui.main.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freese.databinding.FragmentHistoryBinding
import com.example.freese.viewmodel.TransactionViewModel
import com.example.freese.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {

   private var _binding: FragmentHistoryBinding? = null
   private val binding get() = _binding!!

   // Gunakan TransactionViewModel yang sudah kita buat
   private val viewModel: TransactionViewModel by viewModels()
   private lateinit var historyAdapter: HistoryAdapter

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentHistoryBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      setupRecyclerView()
      setupObservers()

      // Panggil API untuk mengambil data riwayat saat halaman pertama dibuka
      viewModel.getHistory()
   }
   override fun onResume() {
      super.onResume()
      viewModel.getHistory()
   }

   private fun setupRecyclerView() {
      historyAdapter = HistoryAdapter(
         onCancelClick = { transactionId ->
            showConfirmDialog("Batalkan Pesanan", "Apakah Anda yakin ingin membatalkan pesanan ini?") {
               viewModel.cancelTransaction(transactionId)
            }
         },
         onCompleteClick = { transactionId ->
            showConfirmDialog("Pesanan Diterima", "Pastikan barang sudah Anda terima dengan baik. Selesaikan pesanan?") {
               viewModel.completeTransaction(transactionId)
            }
         },
         // TANGKAP KLIK TOMBOL BAYAR
         onPayClick = { transactionId ->
            viewModel.getPaymentUrl(transactionId) // Minta URL Midtrans ke Backend
         }
      )

      binding.rvHistory.apply {
         layoutManager = LinearLayoutManager(requireContext())
         adapter = historyAdapter
         setHasFixedSize(true)
      }
   }

   private fun showConfirmDialog(title: String, message: String, onConfirm: () -> Unit) {
      AlertDialog.Builder(requireContext())
         .setTitle(title)
         .setMessage(message)
         .setPositiveButton("Ya") { dialog, _ ->
            onConfirm()
            dialog.dismiss()
         }
         .setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
         }
         .show()
   }

   private fun setupObservers() {
      // 1. Observer untuk Daftar Riwayat
      lifecycleScope.launch {
         viewModel.historyState.collect { result ->
            when (result) {
               is Result.Loading -> {
                  binding.progressBar.visibility = View.VISIBLE
                  binding.rvHistory.visibility = View.GONE
                  binding.tvEmpty.visibility = View.GONE
               }
               is Result.Success -> {
                  binding.progressBar.visibility = View.GONE
                  val historyList = result.data

                  if (historyList.isEmpty()) {
                     binding.tvEmpty.visibility = View.VISIBLE
                     binding.rvHistory.visibility = View.GONE
                  } else {
                     binding.tvEmpty.visibility = View.GONE
                     binding.rvHistory.visibility = View.VISIBLE
                     // Masukkan data ke adapter
                     historyAdapter.submitList(historyList)
                  }
               }
               is Result.Error -> {
                  binding.progressBar.visibility = View.GONE
                  Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
               }
               null -> {}
            }
         }
      }

      // 2. Observer untuk Aksi Tombol (Cancel / Complete)
      lifecycleScope.launch {
         viewModel.actionState.collect { result ->
            if (result is Result.Loading) {
               // Bisa tampilkan loading dialog jika mau
            } else if (result is Result.Success) {
               // Tampilkan pesan sukses dari API
               Toast.makeText(requireContext(), result.data.msg, Toast.LENGTH_SHORT).show()

               // REFRESH DATA: Panggil getHistory lagi agar UI langsung terupdate!
               viewModel.getHistory()

               // Reset state agar aman
               viewModel.resetActionState()
            } else if (result is Result.Error) {
               Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
               viewModel.resetActionState()
            }
         }
      }

      lifecycleScope.launch {
         viewModel.paymentUrlState.collect { result ->
            when (result) {
               is Result.Loading -> {
                  // Munculkan loading/kunci layar jika perlu
                  binding.progressBar.visibility = View.VISIBLE
               }
               is Result.Success -> {
                  binding.progressBar.visibility = View.GONE

                  val paymentUrl = result.data.paymentUrl

                  // KEAJAIBAN: Buka link Midtrans di Browser bawaan HP (Chrome, dll)
                  val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl))
                  startActivity(intent)

                  // Reset agar tidak terbuka berulang kali
                  viewModel.resetPaymentUrlState()
               }
               is Result.Error -> {
                  binding.progressBar.visibility = View.GONE
                  Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                  viewModel.resetPaymentUrlState()
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