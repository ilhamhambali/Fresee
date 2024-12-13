package com.example.freese.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freese.data.ProductModel
import com.example.freese.databinding.FragmentHomeBinding
import com.example.freese.ui.ViewModelFactory
import com.example.freese.ui.detail.DetailActivity
import com.example.freese.ui.main.option.account.AccountActivity
import com.example.freese.ui.search.SearchResultActivity

class HomeFragment : Fragment() {

   private var _binding: FragmentHomeBinding? = null
   private val binding get() = _binding!!

   private lateinit var sayurAdapter: ProductAdapter
   private lateinit var buahAdapter: ProductAdapter

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentHomeBinding.inflate(inflater, container, false)

      binding.edSearch.setOnEditorActionListener { v, actionId, event ->
         if (actionId == EditorInfo.IME_ACTION_SEARCH ||
            (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {

            val query = binding.edSearch.text.toString()
            if (query.isNotEmpty()) {
               val intent = Intent(requireContext(), SearchResultActivity::class.java)
               intent.putExtra("QUERY", query)
               startActivity(intent)
            } else {
               Toast.makeText(requireContext(), "Please enter a search term", Toast.LENGTH_SHORT).show()
            }
            true
         } else {
            false
         }
      }

      binding.ivProfile.setOnClickListener{
         val intent = Intent(requireContext(), AccountActivity::class.java)
         startActivity(intent)
      }

      setupRecyclerView()
      loadDummyData()
      return binding.root
   }

   private fun setupRecyclerView() {
      buahAdapter = ProductAdapter()
      sayurAdapter = ProductAdapter()
      buahAdapter.setOnItemClickListener { product ->
         val intent = Intent(requireContext(), DetailActivity::class.java)
         intent.putExtra("EXTRA_PRODUCT", product)
         startActivity(intent)
      }

      sayurAdapter.setOnItemClickListener { product ->
         val intent = Intent(requireContext(), DetailActivity::class.java)
         intent.putExtra("EXTRA_PRODUCT", product)
         startActivity(intent)
      }
      // Setup RecyclerView Buah
      binding.rvBuah.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      binding.rvBuah.setHasFixedSize(true)
      binding.rvBuah.adapter = buahAdapter

      // Setup RecyclerView Sayur
      binding.rvSayur.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
      binding.rvSayur.setHasFixedSize(true)
      binding.rvSayur.adapter = sayurAdapter
   }

   private fun loadDummyData() {
      // Data Dummy Buah
         val buahList = listOf(
         ProductModel("1", "https://assets.unileversolutions.com/v1/36333896.jpg", "Apel", "Buah Segar", "Abah Anom", "Kota Bandung", "10", "buah","Rp.20.000"),
         ProductModel("2", "https://cdn.rri.co.id/berita/Meulaboh/o/1715939134907-47FFC1F9-C157-4FB3-A126-FFC3D6997C76/bd48p4ouqqa45dd.jpeg", "Anggur", "Buah Segar", "Abah Anom", "Kota Bandung", "10", "buah","Rp.10.000"),
         ProductModel("3", "https://cdn.rri.co.id/berita/Cirebon/o/1720415131736-WhatsApp_Image_2024-07-08_at_10.12.59/4moixi4tmdt7m6g.jpeg", "Semangka", "Buah Segar", "Admin", "Kota Bandung", "10", "buah","Rp.9.000"),
      )

      // Data Dummy Sayur
      val sayurList = listOf(
         ProductModel("4", "https://asset.kompas.com/crops/VlWtNcDY4uvGaLzABWEZkYeZ9zY=/0x0:3234x2156/1200x800/data/photo/2021/09/26/614ff48a45114.jpg", "Kol", "Sayur Segar", "Abah Anom", "Kota Bandung", "10", "buah","Rp.20.000"),
         ProductModel("5", "https://cdn.rri.co.id/berita/Bengkalis/o/1719270001244-WhatsApp_Image_2024-06-25_at_05.56.50/arw5ni8nzj9mqfx.jpeg", "Wortel", "Buah Segar", "Abah Anom", "Kota Bandung", "10", "buah","Rp.10.000"),
         ProductModel("6", "https://asset-a.grid.id/crop/0x0:0x0/780x800/photo/bobofoto/original/4560_fakta-unik-brokoli.jpg", "Brokoli", "Buah Segar", "Abah Anom", "Kota Bandung", "10", "buah","Rp.9.000"),
      )

      // Set data ke adapter
      buahAdapter.setListProduct(buahList)
      sayurAdapter.setListProduct(sayurList)
   }



   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}


