package com.example.freese.ui.sellermain.sellerproduct

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freese.databinding.FragmentSellerProductBinding
import com.example.freese.viewmodel.ProductViewModel
import com.example.freese.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SellerProductsFragment : Fragment() {

   private var _binding: FragmentSellerProductBinding? = null
   private val binding get() = _binding!!

   private val viewModel: ProductViewModel by viewModels()
   private lateinit var adapter: SellerProductAdapter

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
      _binding = FragmentSellerProductBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      setupRecyclerView()
      setupObservers()

      binding.fabAddProduct.setOnClickListener {
         // Buka form tanpa bawa data (Mode Tambah)
         startActivity(Intent(requireContext(), AddEditProductActivity::class.java))
      }
   }

   private fun setupRecyclerView() {
      adapter = SellerProductAdapter(
         onEditClick = { product ->
            val intent = Intent(requireContext(), AddEditProductActivity::class.java)
            intent.putExtra("EXTRA_PRODUCT", product)
            startActivity(intent)
         },
         onDeleteClick = { product ->
            showDeleteConfirmDialog(product.id, product.name)
         }
      )
      binding.rvMyProducts.layoutManager = LinearLayoutManager(requireContext())
      binding.rvMyProducts.adapter = adapter
   }

   private fun showDeleteConfirmDialog(productId: Int, productName: String) {
      AlertDialog.Builder(requireContext())
         .setTitle("Hapus Produk")
         .setMessage("Yakin ingin menghapus $productName dari toko Anda?")
         .setPositiveButton("Hapus") { dialog, _ ->
            viewModel.deleteProduct(productId)
            dialog.dismiss()
         }
         .setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
         }
         .show()
   }

   private fun setupObservers() {
      // Observer Load Produk
      lifecycleScope.launch {
         viewModel.myProductsState.collect { result ->
            when (result) {
               is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
               is Result.Success -> {
                  binding.progressBar.visibility = View.GONE
                  adapter.submitList(result.data)
               }
               is Result.Error -> {
                  binding.progressBar.visibility = View.GONE
                  Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
               }
               null -> {}
            }
         }
      }

      // Observer Delete Produk
      lifecycleScope.launch {
         viewModel.deleteProductState.collect { result ->
            if (result is Result.Success) {
               Toast.makeText(requireContext(), "Produk dihapus", Toast.LENGTH_SHORT).show()
               viewModel.getMyProducts() // Refresh daftar otomatis
               viewModel.resetDeleteState()
            } else if (result is Result.Error) {
               Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
               viewModel.resetDeleteState()
            }
         }
      }
   }

   override fun onResume() {
      super.onResume()
      viewModel.getMyProducts()
   }
   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}