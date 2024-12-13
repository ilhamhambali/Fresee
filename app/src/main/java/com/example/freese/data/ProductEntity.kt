package com.example.freese.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class ProductEntity(
   @PrimaryKey(autoGenerate = true)
   @ColumnInfo(name = "id")
   var id: Int = 0,

   @ColumnInfo(name = "imageResId")
   var imageResId: String? =null,

   @ColumnInfo(name = "title")
   var title: String?=null,

   @ColumnInfo(name = "description")
   var description: String?=null,

   @ColumnInfo(name = "seller")
   var seller: String?=null,

   @ColumnInfo(name = "cityName")
   var cityName: String?=null,

   @ColumnInfo(name = "stok")
   var stok: String?=null,

   @ColumnInfo(name = "category")
   var category: String?=null,

   @ColumnInfo(name = "price")
   var price: String?=null
): Parcelable