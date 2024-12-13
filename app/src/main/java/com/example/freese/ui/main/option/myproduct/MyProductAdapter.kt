package com.example.freese.ui.main.option.myproduct

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freese.data.ProductModel
import com.example.freese.databinding.ItemMyproductBinding

class MyProductAdapter : RecyclerView.Adapter<MyProductAdapter.MyProductViewHolder>() {

   private val productList = mutableListOf<ProductModel>()

   private var onItemClickListener: ((ProductModel) -> Unit)? = null

   fun setListProduct(products: List<ProductModel>) {
      productList.clear()
      productList.addAll(products)
      notifyDataSetChanged()
   }
   fun setOnItemClickListener(listener: (ProductModel) -> Unit) {
      onItemClickListener = listener
   }
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyProductViewHolder {
      val binding = ItemMyproductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return MyProductViewHolder(binding)
   }

   override fun onBindViewHolder(holder: MyProductViewHolder, position: Int) {
      val product = productList[position]
      holder.bind(product)
   }

   override fun getItemCount(): Int = productList.size

   inner class MyProductViewHolder(private val binding: ItemMyproductBinding) : RecyclerView.ViewHolder(binding.root) {
      fun bind(product: ProductModel) {
         binding.tvFavTitle .text = product.name
         binding.tvFavPrice.text = product.price
         Glide.with(binding.root.context)
            .load(product.imageRes)
            .into(binding.imgFav)
         binding.root.setOnClickListener {
            onItemClickListener?.invoke(product)
         }
      }
   }
}

