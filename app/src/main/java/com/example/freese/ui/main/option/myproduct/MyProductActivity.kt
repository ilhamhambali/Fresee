package com.example.freese.ui.main.option.myproduct

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freese.data.remote.model.ProductModel
import com.example.freese.databinding.ActivityMyProductBinding
import com.example.freese.ui.detail.DetailActivity

class MyProductActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMyProductBinding
   private lateinit var buahAdapter: MyProductAdapter

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityMyProductBinding.inflate(layoutInflater)
      setContentView(binding.root)

      setupRecyclerView()

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


}