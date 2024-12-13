package com.example.freese.ui.main.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.freese.data.ProductEntity
import com.example.freese.data.repository.ProductRepository

class HomeViewModel(application: Application) : ViewModel() {
   private val mProductRepository: ProductRepository = ProductRepository(application)

   fun getProductsByCategory(category: String): LiveData<List<ProductEntity>> =
      mProductRepository.getProductsByCategory(category)
}