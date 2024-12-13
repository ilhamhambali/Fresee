package com.example.freese.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.freese.data.pref.FavoritePreference

class DetailViewModel(application: Application) : AndroidViewModel(application) {

   private val preferencesHelper = FavoritePreference(application)

   private val _isFavorite = MutableLiveData<Boolean>()
   val isFavorite: LiveData<Boolean> get() = _isFavorite

   fun loadFavoriteStatus(productId: String) {
      _isFavorite.value = preferencesHelper.getFavoriteStatus(productId)
   }

   fun toggleFavorite(productId: String) {
      val newStatus = !(_isFavorite.value ?: false)
      _isFavorite.value = newStatus
      preferencesHelper.saveFavoriteStatus(productId, newStatus)
   }
}

