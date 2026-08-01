package com.example.freese.ui.main.dapur


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.freese.databinding.FragmentDapurPintarBinding
import com.example.freese.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// Langkah 5 (Kotlin): Implementasi Logika di Fragment
@AndroidEntryPoint
class DapurPintarFragment : Fragment() {

   private var _binding: FragmentDapurPintarBinding? = null
   private val binding get() = _binding!!

   private val viewModel: DapurPintarViewModel by viewModels()
   private lateinit var inventarisAdapter: InventarisAdapter

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentDapurPintarBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      setupRecyclerView()
      setupObservers()

      binding.fabAddFruit.setOnClickListener {
         // Logika untuk menampilkan dialog tambah buah akan ditambahkan di sini
         Toast.makeText(context, "Fitur tambah buah akan datang!", Toast.LENGTH_SHORT).show()
      }
   }

   private fun setupRecyclerView() {
      inventarisAdapter = InventarisAdapter()
      binding.rvFruitInventory.adapter = inventarisAdapter
   }

   private fun setupObservers() {
      viewLifecycleOwner.lifecycleScope.launch {
         viewModel.inventarisState.collect { result ->
            when (result) {
               is Result.Loading -> {
                  binding.progressBar.visibility = View.VISIBLE
                  binding.tvEmptyState.visibility = View.GONE
               }
               is Result.Success -> {
                  binding.progressBar.visibility = View.GONE
                  if (result.data.isEmpty()) {
                     binding.tvEmptyState.visibility = View.VISIBLE
                     binding.rvFruitInventory.visibility = View.GONE
                  } else {
                     binding.tvEmptyState.visibility = View.GONE
                     binding.rvFruitInventory.visibility = View.VISIBLE
                     inventarisAdapter.submitList(result.data)
                  }
               }
               is Result.Error -> {
                  binding.progressBar.visibility = View.GONE
                  Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                  binding.tvEmptyState.text = result.message
                  binding.tvEmptyState.visibility = View.VISIBLE
               }
            }
         }
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}