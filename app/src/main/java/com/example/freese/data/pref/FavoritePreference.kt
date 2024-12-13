package com.example.freese.data.pref

import android.content.Context
import android.content.SharedPreferences

class FavoritePreference(context: Context) {
   private val sharedPreferences: SharedPreferences =
      context.getSharedPreferences("favorite_prefs", Context.MODE_PRIVATE)

   fun saveFavoriteStatus(productId: String, isFavorite: Boolean) {
      sharedPreferences.edit()
         .putBoolean(productId, isFavorite)
         .apply()
   }

   fun getFavoriteStatus(productId: String): Boolean {
      return sharedPreferences.getBoolean(productId, false)
   }
}
