package com.example.freese.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.freese.ui.main.home.HomeViewModel
import com.example.freese.ui.main.option.sell.SellViewModel

class ViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {
   companion object {
      @Volatile
      private var INSTANCE: ViewModelFactory? = null
      @JvmStatic
      fun getInstance(application: Application): ViewModelFactory {
         if (INSTANCE == null) {
            synchronized(ViewModelFactory::class.java) {
               INSTANCE = ViewModelFactory(application)
            }
         }
         return INSTANCE as ViewModelFactory
      }
   }

   @Suppress("UNCHECKED_CAST")
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
         return HomeViewModel(mApplication) as T
      } else if (modelClass.isAssignableFrom(SellViewModel::class.java)) {
         return SellViewModel(mApplication) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
   }
}