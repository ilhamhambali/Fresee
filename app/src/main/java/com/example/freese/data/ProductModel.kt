package com.example.freese.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
   val id: String,
   val imageRes: String,
   val name: String,
   val description: String,
   val seller: String,
   val cityName: String,
   val stok: String,
   val category: String,
   val price: String,
) : Parcelable



