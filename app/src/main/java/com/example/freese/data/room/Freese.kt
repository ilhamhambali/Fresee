package com.example.freese.data.room

import android.app.Application
import androidx.room.Room

class FreeseDB : Application() {
   val database by lazy {
      Room.databaseBuilder(this, ProductDatabase::class.java, "product_database")
         .fallbackToDestructiveMigration() // Menghapus data jika skema berubah (untuk pengembangan)
         .build()
   }
}
