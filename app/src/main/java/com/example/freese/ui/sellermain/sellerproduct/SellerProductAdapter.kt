package com.example.freese.ui.sellermain.sellerproduct

import ProductResponse
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freese.databinding.ItemSellerProductBinding
import java.text.NumberFormat
import java.util.Locale

class SellerProductAdapter(
   private val onEditClick: (ProductResponse) -> Unit,
   private val onDeleteClick: (ProductResponse) -> Unit
) : ListAdapter<ProductResponse, SellerProductAdapter.ViewHolder>(DIFF_CALLBACK) {

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      val binding = ItemSellerProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ViewHolder(binding)
   }

   override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bind(getItem(position))
   }

   inner class ViewHolder(private val binding: ItemSellerProductBinding) :
      RecyclerView.ViewHolder(binding.root) {

      fun bind(product: ProductResponse) {
         binding.apply {
            tvName.text = product.name
            tvStock.text = "Stok: ${product.stock}"

            val formatRupiah = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            formatRupiah.maximumFractionDigits = 0
            tvPrice.text = formatRupiah.format(product.price)

            val imageUrl = "http://192.168.100.32:3000/uploads/${product.image}"
            Glide.with(itemView.context)
               .load(imageUrl)
               .centerCrop()
               .into(ivProduct)

            btnEdit.setOnClickListener { onEditClick(product) }
            btnDelete.setOnClickListener { onDeleteClick(product) }
         }
      }
   }

   companion object {
      val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductResponse>() {
         override fun areItemsTheSame(oldItem: ProductResponse, newItem: ProductResponse) = oldItem.id == newItem.id
         override fun areContentsTheSame(oldItem: ProductResponse, newItem: ProductResponse) = oldItem == newItem
      }
   }
}