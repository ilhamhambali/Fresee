package com.example.freese.ui.main.history

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freese.R
import com.example.freese.data.remote.api.response.HistoryResponse
import com.example.freese.databinding.ItemHistoryBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(
   private val onCancelClick: (Int) -> Unit,
   private val onCompleteClick: (Int) -> Unit,
   private val onPayClick: (Int) -> Unit
) : ListAdapter<HistoryResponse, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
      val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return HistoryViewHolder(binding)
   }

   override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
      holder.bind(getItem(position))
   }

   inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
      RecyclerView.ViewHolder(binding.root) {

      fun bind(history: HistoryResponse) {
         binding.apply {
            tvOrderId.text = "Belanja - #${history.id}"

            // Format Tanggal (Sederhana dari ISO 8601)
            try {
               val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
               val formatter = SimpleDateFormat("dd MMM yyyy", Locale("in", "ID"))
               val date = parser.parse(history.createdAt)
               tvDate.text = if (date != null) formatter.format(date) else history.createdAt
            } catch (e: Exception) {
               tvDate.text = history.createdAt
            }

            // Format Harga
            val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            formatRupiah.maximumFractionDigits = 0
            tvTotalPrice.text = formatRupiah.format(history.totalPrice)

            // Menampilkan Ringkasan Produk (Ambil item pertama saja untuk thumbnail)
            if (history.items.isNotEmpty()) {
               val firstItem = history.items[0]
               tvProductName.text = firstItem.product.name

               // Load gambar
               Glide.with(itemView.context)
                  .load(firstItem.product.image)
                  .placeholder(R.drawable.ic_logo_text) // Sesuaikan icon Anda
                  .into(ivProduct)

               // Jika beli lebih dari 1 macam barang, tampilkan teks tambahan
               if (history.items.size > 1) {
                  tvMoreItems.visibility = View.VISIBLE
                  tvMoreItems.text = "+ ${history.items.size - 1} produk lainnya"
               } else {
                  tvMoreItems.visibility = View.GONE
               }
            }

            // --- LOGIKA STATUS DAN TOMBOL AKSI ---
            tvStatus.text = history.status

            // Reset visibilitas awal
            btnCancel.visibility = View.GONE
            btnComplete.visibility = View.GONE
            btnPay.visibility = View.GONE

            when (history.status) {
               "UNPAID" -> {
                  btnPay.visibility = View.VISIBLE
                  btnPay.text = "Bayar Sekarang"
                  btnPay.setOnClickListener {
                     onPayClick(history.id) // Panggil aksi bayar
                  }

                  // Tombol cancel juga boleh dimunculkan jika masih UNPAID/PENDING
                  btnCancel.visibility = View.VISIBLE
                  btnCancel.setOnClickListener { onCancelClick(history.id) }
               }
               "PENDING" -> {
                  tvStatus.setTextColor(Color.parseColor("#FF9800")) // Orange
                  btnCancel.visibility = View.VISIBLE
               }
               "PAID" -> {
                  tvStatus.setTextColor(Color.parseColor("#4CAF50")) // Hijau
                  // Menunggu penjual mengirim barang, tidak ada tombol
               }
               "PROCESSING" -> {
                  tvStatus.setTextColor(Color.parseColor("#2196F3")) // Biru
                  btnComplete.visibility = View.VISIBLE
                  btnComplete.setOnClickListener { onCompleteClick(history.id) }
               }
               "COMPLETED" -> {
                  tvStatus.setTextColor(Color.parseColor("#8BC34A")) // Hijau Terang
               }
               "CANCELLED" -> {
                  tvStatus.setTextColor(Color.parseColor("#F44336")) // Merah
               }
            }

            // Aksi Klik Tombol
            btnCancel.setOnClickListener {
               onCancelClick(history.id)
            }

            btnComplete.setOnClickListener {
               onCompleteClick(history.id)
            }
         }
      }
   }

   companion object {
      val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryResponse>() {
         override fun areItemsTheSame(oldItem: HistoryResponse, newItem: HistoryResponse): Boolean {
            return oldItem.id == newItem.id
         }

         override fun areContentsTheSame(oldItem: HistoryResponse, newItem: HistoryResponse): Boolean {
            return oldItem == newItem
         }
      }
   }
}