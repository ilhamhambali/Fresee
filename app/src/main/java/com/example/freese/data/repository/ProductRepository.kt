package com.example.freese.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.freese.data.AuthResponse
import com.example.freese.data.ProductEntity
import com.example.freese.data.pref.UserModel
import com.example.freese.data.pref.UserPreference
import com.example.freese.data.room.ProductDatabase
import com.example.freese.data.room.dao.ProductDao
import com.example.freese.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ProductRepository(application: Application) {
   private val mProductDao: ProductDao
   private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
   init {
      val db = ProductDatabase.getDatabase(application)
      mProductDao = db.productDao()
   }
   fun getProductsByCategory(category: String): LiveData<List<ProductEntity>> {
      return mProductDao.getProductsByCategory(category)
   }
   fun insert(productEntity: ProductEntity) {
      executorService.execute { mProductDao.insert(productEntity) }
   }
   fun delete(productEntity: ProductEntity) {
      executorService.execute { mProductDao.delete(productEntity) }
   }
   fun update(productEntity: ProductEntity) {
      executorService.execute { mProductDao.update(productEntity) }
   }
}
