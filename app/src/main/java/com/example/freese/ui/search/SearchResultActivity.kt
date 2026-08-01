package com.example.freese.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.freese.databinding.ActivitySearchResultBinding
import com.example.freese.ui.detail.DetailActivity
import com.example.freese.ui.main.home.ProductAdapter
import com.example.freese.viewmodel.ProductViewModel
import com.example.freese.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.getValue

@AndroidEntryPoint
class SearchResultActivity : AppCompatActivity() {

   private lateinit var binding: ActivitySearchResultBinding

   // Kita gunakan ulang ProductViewModel!
   private val viewModel: ProductViewModel by viewModels()
   private lateinit var productAdapter: ProductAdapter

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivitySearchResultBinding.inflate(layoutInflater)
      setContentView(binding.root)

      setupRecyclerView()
      setupSearchView()
      setupObservers()

      // Tombol Back
      binding.btnBack.setOnClickListener {
         onBackPressedDispatcher.onBackPressed()
      }

      // UX Tambahan: Otomatis memunculkan keyboard saat halaman ini dibuka
      binding.searchView.requestFocus()
      val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      imm.showSoftInput(binding.searchView.findFocus(), InputMethodManager.SHOW_IMPLICIT)
   }

   private fun setupRecyclerView() {
      productAdapter = ProductAdapter { clickedProduct ->
         // Buka DetailActivity saat produk diklik
         val intent = Intent(this, DetailActivity::class.java)
         intent.putExtra("EXTRA_PRODUCT", clickedProduct)
         startActivity(intent)
      }

      // Tampilkan hasil pencarian dalam bentuk Grid (2 Kolom) seperti di Home
      binding.rvSearchResults.apply {
         layoutManager = GridLayoutManager(this@SearchResultActivity, 2)
         adapter = productAdapter
         setHasFixedSize(true)
      }
   }

   private fun setupSearchView() {
      binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

         // Saat user menekan Enter / Ikon Search di keyboard
         override fun onQueryTextSubmit(query: String?): Boolean {
            if (!query.isNullOrEmpty()) {
               // Panggil API Pencarian
               viewModel.getAllProducts(search = query)
            }

            // Sembunyikan keyboard
            binding.searchView.clearFocus()
            return true
         }

         // (Opsional) Saat user mengetik huruf demi huruf
         override fun onQueryTextChange(newText: String?): Boolean {
            // Jika user menghapus ketikan sampai kosong
            if (newText.isNullOrEmpty()) {
               // Kosongkan daftar (Atau bisa memanggil getAllProducts(null) jika ingin tampil semua)
               productAdapter.submitList(emptyList())
               binding.layoutEmpty.visibility = View.GONE
            }
            return true
         }
      })
   }

   private fun setupObservers() {
      lifecycleScope.launch {
         viewModel.allProductsState.collect { result ->
            when (result) {
               is Result.Loading -> {
                  binding.progressBar.visibility = View.VISIBLE
                  binding.rvSearchResults.visibility = View.GONE
                  binding.layoutEmpty.visibility = View.GONE
               }
               is Result.Success -> {
                  binding.progressBar.visibility = View.GONE
                  val productList = result.data

                  if (productList.isEmpty()) {
                     binding.layoutEmpty.visibility = View.VISIBLE
                     binding.rvSearchResults.visibility = View.GONE
                  } else {
                     binding.layoutEmpty.visibility = View.GONE
                     binding.rvSearchResults.visibility = View.VISIBLE
                     productAdapter.submitList(productList)
                  }
               }
               is Result.Error -> {
                  binding.progressBar.visibility = View.GONE
                  Toast.makeText(this@SearchResultActivity, result.message, Toast.LENGTH_LONG).show()
               }
               null -> {}
            }
         }
      }
   }
}