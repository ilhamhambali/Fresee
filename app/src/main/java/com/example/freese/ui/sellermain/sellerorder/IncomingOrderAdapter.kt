package com.example.freese.ui.sellermain.sellerorder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freese.data.remote.api.response.IncomingOrderResponse
import com.example.freese.databinding.ItemIncomingOrderBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.core.graphics.toColorInt

class IncomingOrderAdapter(
   private val onSendClick: (Int) -> Unit // Mengirim ID Transaksi
) : ListAdapter<IncomingOrderResponse, IncomingOrderAdapter.OrderViewHolder>(DIFF_CALLBACK) {

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
      val binding = ItemIncomingOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return OrderViewHolder(binding)
   }

   override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
      holder.bind(getItem(position))
   }

   inner class OrderViewHolder(private val binding: ItemIncomingOrderBinding) :
      RecyclerView.ViewHolder(binding.root) {

      fun bind(order: IncomingOrderResponse) {
         binding.apply {
            val transaction = order.transaction
            tvOrderId.text = "Order - #${transaction.id}"

            // Format Tanggal
            try {
               val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
               val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("in", "ID"))
               val date = parser.parse(transaction.createdAt)
               tvDate.text = if (date != null) formatter.format(date) else transaction.createdAt
            } catch (e: Exception) {
               tvDate.text = transaction.createdAt
            }

            // Info Pembeli
            tvBuyerName.text = transaction.user.fullName
            tvBuyerAddress.text = transaction.user.address

            // Info Produk
            tvProductName.text = order.product.name
            val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            formatRupiah.maximumFractionDigits = 0
            tvQtyPrice.text = "${order.quantity} x ${formatRupiah.format(order.price)}"

            val imageUrl = "http://192.168.100.32:3000/uploads/${order.product.image}"
            Glide.with(itemView.context).load(imageUrl).into(ivProduct)

            // Logika Status & Tombol
            tvStatus.text = transaction.status
            btnSend.visibility = View.GONE

            when (transaction.status) {
               "PENDING" -> tvStatus.setTextColor("#FF9800".toColorInt()) // Orange
               "PAID" -> {
                  tvStatus.setTextColor("#4CAF50".toColorInt()) // Hijau
                  btnSend.visibility = View.VISIBLE // Tombol Kirim muncul!
               }
               "SENT" -> tvStatus.setTextColor("#2196F3".toColorInt()) // Biru
               "COMPLETED" -> tvStatus.setTextColor("#8BC34A".toColorInt()) // Hijau Terang
               "CANCELLED" -> tvStatus.setTextColor("#F44336".toColorInt()) // Merah
            }

            // Aksi klik Kirim Barang
            btnSend.setOnClickListener {
               onSendClick(transaction.id) // Kirim ID transaksi ke Fragment
            }
         }
      }
   }

   companion object {
      val DIFF_CALLBACK = object : DiffUtil.ItemCallback<IncomingOrderResponse>() {
         override fun areItemsTheSame(oldItem: IncomingOrderResponse, newItem: IncomingOrderResponse) = oldItem.id == newItem.id
         override fun areContentsTheSame(oldItem: IncomingOrderResponse, newItem: IncomingOrderResponse) = oldItem == newItem
      }
   }
}