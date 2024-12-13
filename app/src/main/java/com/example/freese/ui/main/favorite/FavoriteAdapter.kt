package com.example.freese.ui.main.favorite

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freese.R
import com.example.freese.data.ProductModel
import com.example.freese.databinding.ItemFavoriteBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoriteAdapter : ListAdapter<ProductModel, FavoriteAdapter.FavoriteViewHolder>(DiffCallback()) {
   private var onItemClickListener: ((ProductModel) -> Unit)? = null
   fun setOnItemClickListener(listener: (ProductModel) -> Unit) {
      onItemClickListener = listener
   }
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
      val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return FavoriteViewHolder(binding)
   }

   override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
      val favorite = getItem(position)
      holder.bind(favorite)
   }

   inner class FavoriteViewHolder(private val binding: ItemFavoriteBinding) :
      RecyclerView.ViewHolder(binding.root) {

      fun bind(product: ProductModel) {
         with(binding) {
            tvFavTitle.text = product.name
            tvFavPrice.text = product.price
            Glide.with(binding.root.context)
               .load(product.imageRes)
               .into(binding.imgFav)
            imgFav.setImageURI(Uri.parse(product.imageRes))
            root.setOnClickListener {
               onItemClickListener?.invoke(product)
            }
            // Cek status favorit
            val isFavorite = isProductFavorite(product)
            updateFavoriteIcon(isFavorite)

            // Tambahkan listener untuk tombol favorit
            btnFavorite.setOnClickListener {
               val newStatus = !isFavorite
               updateFavoriteStatus(product, newStatus)
               updateFavoriteIcon(newStatus)
            }
         }
      }

      private fun isProductFavorite(product: ProductModel): Boolean {
         val sharedPref = binding.root.context.getSharedPreferences("FAVORITES_PREF", Context.MODE_PRIVATE)
         val gson = Gson()
         val type = object : TypeToken<List<ProductModel>>() {}.type
         val favorites: List<ProductModel> = gson.fromJson(
            sharedPref.getString("FAVORITES_LIST", "[]"), type
         ) ?: emptyList()
         return favorites.any { it.name == product.name } // Gunakan id jika ada
      }

      private fun updateFavoriteStatus(product: ProductModel, isFavorite: Boolean) {
         val sharedPref = binding.root.context.getSharedPreferences("FAVORITES_PREF", Context.MODE_PRIVATE)
         val editor = sharedPref.edit()
         val gson = Gson()
         val type = object : TypeToken<MutableList<ProductModel>>() {}.type
         val favorites: MutableList<ProductModel> = gson.fromJson(
            sharedPref.getString("FAVORITES_LIST", "[]"), type
         ) ?: mutableListOf()

         if (isFavorite) {
            // Tambahkan jika belum ada
            if (!favorites.any { it.name == product.name }) {
               favorites.add(product)
            }
         } else {
            // Hapus dari daftar
            favorites.removeAll { it.name == product.name }
         }

         // Simpan kembali
         editor.putString("FAVORITES_LIST", gson.toJson(favorites))
         editor.apply()
      }

      private fun updateFavoriteIcon(isFavorite: Boolean) {
         val color = if (isFavorite) {
            R.color.red
         } else {
            R.color.text_primary
         }
         binding.btnFavorite.setColorFilter(
            ContextCompat.getColor(binding.root.context, color),
            android.graphics.PorterDuff.Mode.SRC_IN
         )
      }
   }


   class DiffCallback : DiffUtil.ItemCallback<ProductModel>() {
      override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
         return oldItem.name == newItem.name
      }

      override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
         return oldItem == newItem
      }
   }
}
