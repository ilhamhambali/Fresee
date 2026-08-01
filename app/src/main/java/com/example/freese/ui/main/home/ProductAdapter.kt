package com.example.freese.ui.main.home
import ProductResponse
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freese.R
import com.example.freese.databinding.ItemProductBinding
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
   private val onItemClick: (ProductResponse) -> Unit // Fungsi klik saat item dipilih
) : ListAdapter<ProductResponse, ProductAdapter.ProductViewHolder>(DIFF_CALLBACK) {

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
      val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ProductViewHolder(binding)
   }

   override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
      val product = getItem(position)
      holder.bind(product)
   }

   inner class ProductViewHolder(private val binding: ItemProductBinding) :
      RecyclerView.ViewHolder(binding.root) {

      fun bind(product: ProductResponse) {
         binding.apply {
            tvProductName.text = product.name

            // Format harga ke Rupiah
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            formatRupiah.maximumFractionDigits = 0 // Hilangkan koma di belakang
            tvProductPrice.text = formatRupiah.format(product.price)

            // Tampilkan nama toko atau nama penjual
            val ownerName = product.owner?.farmName ?: product.owner?.fullName ?: "Penjual Tidak Diketahui"
            tvProductOwner.text = ownerName

            // Load gambar pakai Glide
            Glide.with(itemView.context)
               .load(product.image)
               .placeholder(R.drawable.ic_logo_text) // Ganti dengan icon loading Anda
               .centerCrop()
               .into(ivProductImage)

            // Pasang event klik pada seluruh kotak item
            root.setOnClickListener {
               onItemClick(product)
            }
         }
      }
   }

   companion object {
      val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductResponse>() {
         override fun areItemsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean {
            return oldItem.id == newItem.id
         }

         override fun areContentsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean {
            return oldItem == newItem
         }
      }
   }
}