package com.example.freese.ui.main.dapur


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.freese.data.remote.model.InventarisBuah
import com.example.freese.databinding.ItemBuahInventarisBinding
import java.text.SimpleDateFormat
import java.util.Locale

// Adapter untuk RecyclerView
class InventarisAdapter : ListAdapter<InventarisBuah, InventarisAdapter.BuahViewHolder>(DIFF_CALLBACK) {

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuahViewHolder {
      val binding = ItemBuahInventarisBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return BuahViewHolder(binding)
   }

   override fun onBindViewHolder(holder: BuahViewHolder, position: Int) {
      val buah = getItem(position)
      holder.bind(buah)
   }

   inner class BuahViewHolder(private val binding: ItemBuahInventarisBinding) :
      RecyclerView.ViewHolder(binding.root) {
      fun bind(buah: InventarisBuah) {
         binding.tvFruitName.text = buah.namaBuah
         binding.tvRipenessStatus.text = buah.statusKesegaran
         // Format tanggal agar lebih mudah dibaca
         try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(buah.tanggalMasuk)
            binding.tvDateAdded.text = "Ditambahkan: ${outputFormat.format(date)}"
         } catch (e: Exception) {
            binding.tvDateAdded.text = "Ditambahkan: - "
         }
      }
   }

   companion object {
      private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<InventarisBuah>() {
         override fun areItemsTheSame(oldItem: InventarisBuah, newItem: InventarisBuah): Boolean {
            return oldItem.id == newItem.id
         }

         override fun areContentsTheSame(oldItem: InventarisBuah, newItem: InventarisBuah): Boolean {
            return oldItem == newItem
         }
      }
   }
}