package com.example.freese.ui.main.home.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freese.R
import com.example.freese.data.remote.api.response.CartItemResponse
import com.example.freese.databinding.ItemCartBinding
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(
   // Fungsi yang akan dipanggil saat checkbox di-klik
   private val onCheckedChange: (CartItemResponse, Boolean) -> Unit,
   private val onQuantityChange: (CartItemResponse, Int) -> Unit,
   private val onDeleteClick: (CartItemResponse) -> Unit
) : ListAdapter<CartItemResponse, CartAdapter.CartViewHolder>(DIFF_CALLBACK) {

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
      val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return CartViewHolder(binding)
   }

   override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
      val cartItem = getItem(position)
      holder.bind(cartItem)
   }

   inner class CartViewHolder(private val binding: ItemCartBinding) :
      RecyclerView.ViewHolder(binding.root) {

      fun bind(cartItem: CartItemResponse) {
         binding.apply {
            val product = cartItem.product

            tvCartName.text = product.name
            tvCartQuantity.text = "Jumlah: ${cartItem.quantity}"

            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            formatRupiah.maximumFractionDigits = 0
            tvCartPrice.text = formatRupiah.format(product.price)

            val imageUrl = "http://192.168.100.32:3000/uploads/${product.image}"
            Glide.with(itemView.context)
               .load(imageUrl)
               .placeholder(R.drawable.ic_logo_text)
               .centerCrop()
               .into(ivCartImage)

            tvCartQty.text = cartItem.quantity.toString()

            cbItemCart.setOnCheckedChangeListener(null)
            cbItemCart.isChecked = cartItem.isSelected
            cbItemCart.setOnCheckedChangeListener { _, isChecked ->
               onCheckedChange(cartItem, isChecked)
            }

            btnMinQty.setOnClickListener {
               if (cartItem.quantity > 1) {
                  onQuantityChange(cartItem, cartItem.quantity - 1)
               }
            }

            btnPlusQty.setOnClickListener {
               if (cartItem.quantity < cartItem.product.stock) {
                  onQuantityChange(cartItem, cartItem.quantity + 1)
               } else {
                  // Tidak bisa melebihi stok
               }
            }
         }
      }
   }

   companion object {
      val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartItemResponse>() {
         override fun areItemsTheSame(oldItem: CartItemResponse, newItem: CartItemResponse): Boolean {
            return oldItem.id == newItem.id
         }

         override fun areContentsTheSame(oldItem: CartItemResponse, newItem: CartItemResponse): Boolean {
            return oldItem == newItem
         }
      }
   }
}