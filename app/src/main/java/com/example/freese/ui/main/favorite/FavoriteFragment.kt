package com.example.freese.ui.main.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freese.R
import com.example.freese.data.model.ProductModel
import com.example.freese.ui.detail.DetailActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoriteFragment : Fragment() {

   private lateinit var adapter: FavoriteAdapter

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View? {
      val view = inflater.inflate(R.layout.fragment_favorite, container, false)

      // Siapkan RecyclerView
      val recyclerView = view.findViewById<RecyclerView>(R.id.rvFavorite)
      adapter = FavoriteAdapter()
      recyclerView.adapter = adapter
      recyclerView.layoutManager = LinearLayoutManager(requireContext())

      adapter.setOnItemClickListener { product ->
         val intent = Intent(requireContext(), DetailActivity::class.java)
         intent.putExtra("EXTRA_PRODUCT", product)
         startActivity(intent)
      }


      // Ambil data favorit dan tampilkan
      val favorites = getFavorites()
      adapter.submitList(favorites)



      return view
   }

   private fun getFavorites(): List<ProductModel> {
      val sharedPref = requireContext().getSharedPreferences("FAVORITES_PREF", Context.MODE_PRIVATE)
      val gson = Gson()
      val type = object : TypeToken<List<ProductModel>>() {}.type

      return gson.fromJson(sharedPref.getString("FAVORITES_LIST", "[]"), type) ?: emptyList()
   }
}

