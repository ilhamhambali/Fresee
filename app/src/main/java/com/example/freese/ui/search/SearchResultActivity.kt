package com.example.freese.ui.search

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freese.R
import com.example.freese.data.ProductModel
import com.example.freese.databinding.ActivitySearchResultBinding
import com.example.freese.ui.detail.DetailActivity
import com.example.freese.ui.main.home.ProductAdapter

class SearchResultActivity : AppCompatActivity() {

   private lateinit var binding: ActivitySearchResultBinding
   private lateinit var buahAdapter: ProductAdapter


   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivitySearchResultBinding.inflate(layoutInflater)
      setContentView(binding.root)

      val query = intent.getStringExtra("QUERY")

      if (!query.isNullOrEmpty()) {
         findViewById<TextView>(R.id.tv_search_result).text = "$query"
      } else {
         Toast.makeText(this, "No search term provided", Toast.LENGTH_SHORT).show()
      }


      setupRecyclerView()
      loadDummyData()
   }


   private fun setupRecyclerView() {
      buahAdapter = ProductAdapter()
      buahAdapter.setOnItemClickListener { product ->
         val intent = Intent(this, DetailActivity::class.java)
         intent.putExtra("EXTRA_PRODUCT", product)
         startActivity(intent)
      }


      // Setup RecyclerView Buah
      binding.rvSearchResults.layoutManager = GridLayoutManager(this, 2)
      binding.rvSearchResults.setHasFixedSize(true)
      binding.rvSearchResults.adapter = buahAdapter


   }

   private fun loadDummyData() {
      // Data Dummy Buah
      val buahList = listOf(
         ProductModel("1", "https://assets.unileversolutions.com/v1/36333896.jpg", "Apel", "Buah Segar", "Abah Anom", "Kota Bandung", "10", "buah","Rp.20.000"),
         ProductModel("2", "https://cdn.rri.co.id/berita/Meulaboh/o/1715939134907-47FFC1F9-C157-4FB3-A126-FFC3D6997C76/bd48p4ouqqa45dd.jpeg", "Anggur", "Buah Segar", "Abah Anom", "Kota Bandung", "10", "buah","Rp.10.000"),
         ProductModel("3", "https://cdn.rri.co.id/berita/Cirebon/o/1720415131736-WhatsApp_Image_2024-07-08_at_10.12.59/4moixi4tmdt7m6g.jpeg", "Semangka", "Buah Segar", "Abah Anom", "Kota Bandung", "10", "buah","Rp.9.000"),
      )
      // Set data ke adapter
      buahAdapter.setListProduct(buahList)
   }

}
