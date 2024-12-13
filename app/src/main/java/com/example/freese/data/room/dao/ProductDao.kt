package com.example.freese.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.freese.data.ProductEntity
import com.example.freese.data.ProductModel

@Dao
interface ProductDao {
   @Insert(onConflict = OnConflictStrategy.IGNORE)
   fun insert(product: ProductEntity)
   @Update
   fun update(product: ProductEntity)
   @Delete
   fun delete(product: ProductEntity)
   @Query("SELECT * from productentity where category = :category")
   fun getProductsByCategory(category: String): LiveData<List<ProductEntity>>

}