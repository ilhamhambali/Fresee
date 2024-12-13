package com.example.freese.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.freese.data.ProductEntity

class ProductDiffCallback(private val oldProductList: List<ProductEntity>, private val newProductList: List<ProductEntity>) : DiffUtil.Callback() {

   override fun getOldListSize(): Int = oldProductList.size

   override fun getNewListSize(): Int = newProductList.size

   override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      return oldProductList[oldItemPosition].id == newProductList[newItemPosition].id
   }

   override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
      val oldProduct = oldProductList[oldItemPosition]
      val newProduct = newProductList[newItemPosition]
      return oldProduct.imageResId == newProduct.imageResId &&
             oldProduct.title == newProduct.title &&
             oldProduct.description == newProduct.description &&
             oldProduct.seller == newProduct.seller &&
             oldProduct.cityName == newProduct.cityName &&
             oldProduct.stok == newProduct.stok &&
             oldProduct.price == newProduct.price

   }
}