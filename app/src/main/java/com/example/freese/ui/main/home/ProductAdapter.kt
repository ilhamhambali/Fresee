package com.example.freese.ui.main.home
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freese.data.ProductModel
import com.example.freese.databinding.ItemProductBinding

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

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
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
      val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return ProductViewHolder(binding)
   }

   override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
      val product = productList[position]
      holder.bind(product)
   }

   override fun getItemCount(): Int = productList.size

   inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
      fun bind(product: ProductModel) {
         binding.productName.text = product.name
         binding.productPrice.text = product.price
         binding.productSeller.text = product.seller
         binding.productCity.text = product.cityName
         Glide.with(binding.root.context)
            .load(product.imageRes)
            .into(binding.productImage)
         binding.root.setOnClickListener {
            onItemClickListener?.invoke(product)
         }
      }
   }
}

