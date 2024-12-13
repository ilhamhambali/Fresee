package com.example.freese.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.freese.data.ProductEntity
import com.example.freese.data.ProductModel
import com.example.freese.data.room.dao.ProductDao


@Database(entities = [ProductEntity::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {
   abstract fun productDao(): ProductDao

   companion object {
      @Volatile
      private var INSTANCE: ProductDatabase? = null

      @JvmStatic
      fun getDatabase(context: Context): ProductDatabase {
         if (INSTANCE == null) {
            synchronized(ProductDatabase::class.java) {
               INSTANCE = Room.databaseBuilder(context.applicationContext,
                  ProductDatabase::class.java, "product_database")
                  .build()
            }
         }
         return INSTANCE as ProductDatabase
      }
   }
}
