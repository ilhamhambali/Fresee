package com.example.freese.ui.main.home


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.freese.databinding.FragmentHomeBinding
import com.example.freese.ui.detail.DetailActivity
import com.example.freese.ui.search.SearchResultActivity
import com.example.freese.utils.Result
import com.example.freese.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

   private var _binding: FragmentHomeBinding? = null
   private val binding get() = _binding!!

   // Panggil ViewModel yang sudah kita buat di Tahap 3
   private val viewModel: ProductViewModel by viewModels()
   private lateinit var productAdapter: ProductAdapter

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentHomeBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      setupObservers()
      setupSearchView()
      setupRecyclerView()

      // Panggil API untuk mengambil semua produk
      viewModel.getAllProducts()
   }

   private fun setupRecyclerView() {
      productAdapter = ProductAdapter { clickedProduct ->
         // Pindah ke DetailActivity sambil membawa data produk
         val intent = Intent(requireContext(), DetailActivity::class.java)
         intent.putExtra("EXTRA_PRODUCT", clickedProduct)
         startActivity(intent)
      }

      binding.rvRekomendasi.apply {
         layoutManager = GridLayoutManager(requireContext(), 2)
         adapter = productAdapter
         setHasFixedSize(true)
      }
      binding.rvBuah.apply {
         layoutManager = GridLayoutManager(requireContext(), 3)
         adapter = productAdapter
         setHasFixedSize(true)
      }
   }

   private fun setupObservers() {
      lifecycleScope.launch {
         viewModel.allProductsState.collect { result ->
            when (result) {
               is Result.Loading -> {
                  binding.progressBar.visibility = View.VISIBLE
                  binding.rvRekomendasi.visibility = View.GONE
                  binding.rvBuah.visibility = View.GONE
               }
               is Result.Success -> {
                  binding.progressBar.visibility = View.GONE
                  binding.rvRekomendasi.visibility = View.VISIBLE
                  binding.rvBuah.visibility = View.VISIBLE

                  // Masukkan data dari API ke dalam Adapter
                  productAdapter.submitList(result.data)
               }
               is Result.Error -> {
                  binding.progressBar.visibility = View.GONE
                  Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
               }
               null -> {}
            }
         }
      }
   }
   private fun setupSearchView() {
      // Saat user menyentuh kotak pencarian di Home
      binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
         if (hasFocus) {
            // Hilangkan fokus agar keyboard tidak muncul di Home
            binding.searchView.clearFocus()

            // Pindah ke Halaman Pencarian
            val intent = Intent(requireContext(), SearchResultActivity::class.java)
            startActivity(intent)
         }
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}