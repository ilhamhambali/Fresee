package com.example.freese.ui.sellermain.sellerorder

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freese.databinding.FragmentSellerOrderBinding
import com.example.freese.viewmodel.TransactionViewModel
import com.example.freese.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class SellerOrderFragment : Fragment() {

   private var _binding: FragmentSellerOrderBinding? = null
   private val binding get() = _binding!!

   // Menggunakan TransactionViewModel
   private val viewModel: TransactionViewModel by viewModels()
   private lateinit var adapter: IncomingOrderAdapter
   private var selectedTransactionId: Int = -1
   private var selectedResiUri: Uri? = null
   private val pickResiLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
      if (uri != null) {
         selectedResiUri = uri
         showConfirmUploadDialog() // Tampilkan dialog setelah gambar dipilih
      } else {
         selectedTransactionId = -1 // Reset jika user batal pilih foto
      }
   }

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
      _binding = FragmentSellerOrderBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      setupRecyclerView()
      setupObservers()
   }

   // Gunakan onResume agar selalu memuat pesanan terbaru setiap kali halaman dibuka
   override fun onResume() {
      super.onResume()
      viewModel.getIncomingOrders()
   }


   private fun setupRecyclerView() {
      adapter = IncomingOrderAdapter(
         onSendClick = { transactionId ->
            // SIMPAN ID TRANSAKSI LALU BUKA GALERI
            selectedTransactionId = transactionId
            pickResiLauncher.launch("image/*")
         }
      )

      binding.rvOrders.apply {
         layoutManager = LinearLayoutManager(requireContext())
         adapter = this@SellerOrderFragment.adapter
      }
   }

   private fun showConfirmUploadDialog() {
      AlertDialog.Builder(requireContext())
         .setTitle("Upload Resi & Kirim")
         .setMessage("Apakah Anda yakin ingin mengubah status pesanan menjadi SENT dengan foto resi ini?")
         .setPositiveButton("Ya, Kirim") { dialog, _ ->
            uploadResi()
            dialog.dismiss()
         }
         .setNegativeButton("Batal") { dialog, _ ->
            selectedTransactionId = -1
            selectedResiUri = null
            dialog.dismiss()
         }
         .show()
   }

   private fun uploadResi() {
      if (selectedTransactionId == -1 || selectedResiUri == null) return

      // Ubah ID menjadi RequestBody (Sesuai dengan perubahan di Repository)
      val idBody = selectedTransactionId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

      // Siapkan File Foto Invoice (Nama parameter diganti dari "resi" menjadi "invoice")
      val file = getFileFromUri(selectedResiUri!!)
      val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
      val invoicePart = MultipartBody.Part.createFormData("invoice", file.name, reqFile)

      // Tembak API (Parameter statusBody sudah dihapus karena backend otomatis ubah ke PROCESSING)
      viewModel.updateTransactionStatus(idBody, invoicePart)
   }

   // Fungsi bantuan untuk mengubah URI dari galeri menjadi File fisik
   private fun getFileFromUri(uri: Uri): File {
      val inputStream = requireContext().contentResolver.openInputStream(uri)
      val tempFile = File.createTempFile("resi_upload", ".jpg", requireContext().cacheDir)
      val outputStream = FileOutputStream(tempFile)
      inputStream?.copyTo(outputStream)
      inputStream?.close()
      outputStream.close()
      return tempFile
   }
   private fun setupObservers() {
      // 1. Observer untuk memuat daftar pesanan
      lifecycleScope.launch {
         viewModel.incomingOrdersState.collect { result ->
            when (result) {
               is Result.Loading -> {
                  binding.progressBar.visibility = View.VISIBLE
                  binding.rvOrders.visibility = View.GONE
                  binding.tvEmpty.visibility = View.GONE
               }
               is Result.Success -> {
                  binding.progressBar.visibility = View.GONE
                  val orderList = result.data

                  if (orderList.isEmpty()) {
                     binding.tvEmpty.visibility = View.VISIBLE
                     binding.rvOrders.visibility = View.GONE
                  } else {
                     binding.tvEmpty.visibility = View.GONE
                     binding.rvOrders.visibility = View.VISIBLE
                     adapter.submitList(orderList)
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

      // 2. Observer untuk status Update "Kirim Barang"
      lifecycleScope.launch {
         viewModel.actionState.collect { result ->
            if (result is Result.Success) {
               Toast.makeText(requireContext(), "Status berhasil diupdate ke SENT", Toast.LENGTH_SHORT).show()
               // Refresh data pesanan agar tombol hilang dan status berubah
               viewModel.getIncomingOrders()
               viewModel.resetActionState()
            } else if (result is Result.Error) {
               Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
               viewModel.resetActionState()
            }
         }
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}