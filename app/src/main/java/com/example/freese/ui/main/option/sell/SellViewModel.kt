package com.example.freese.ui.main.option.sell

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.freese.data.ProductEntity
import com.example.freese.data.repository.ProductRepository

class SellViewModel(application: Application) : ViewModel() {
   private val mProductRepository: ProductRepository = ProductRepository(application)
   fun insert(product: ProductEntity) {
      mProductRepository.insert(product)
   }
   fun update(product: ProductEntity) {
      mProductRepository.update(product)
   }
   fun delete(product: ProductEntity) {
      mProductRepository.delete(product)
   }
}