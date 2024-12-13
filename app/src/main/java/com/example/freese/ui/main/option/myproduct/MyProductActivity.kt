package com.example.freese.ui.main.option.myproduct

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freese.R
import com.example.freese.data.ProductModel
import com.example.freese.databinding.ActivityMyProductBinding
import com.example.freese.ui.detail.DetailActivity
import com.example.freese.ui.main.home.ProductAdapter

class MyProductActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMyProductBinding
   private lateinit var buahAdapter: MyProductAdapter

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityMyProductBinding.inflate(layoutInflater)
      setContentView(binding.root)

      setupRecyclerView()
      loadDummyData()

   }
   private fun setupRecyclerView() {
      buahAdapter = MyProductAdapter()

      buahAdapter.setOnItemClickListener { product ->
         val intent = Intent(this, DetailActivity::class.java)
         intent.putExtra("EXTRA_PRODUCT", product)
         startActivity(intent)
      }

      binding.rvMyproduct.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
      binding.rvMyproduct.setHasFixedSize(true)
      binding.rvMyproduct.adapter = buahAdapter


   }

   private fun loadDummyData() {
      // Data Dummy Buah
      val buahList = listOf(
         ProductModel("3", "https://cdn.rri.co.id/berita/Cirebon/o/1720415131736-WhatsApp_Image_2024-07-08_at_10.12.59/4moixi4tmdt7m6g.jpeg", "Semangka", "Buah Segar", "Admin", "Kota Bandung", "10", "buah","Rp.9.000"),
         )
      // Set data ke adapter
      buahAdapter.setListProduct(buahList)
   }

}