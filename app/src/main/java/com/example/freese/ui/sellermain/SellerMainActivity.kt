package com.example.freese.ui.sellermain

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.freese.R
import com.example.freese.databinding.ActivitySellerMainBinding
import com.example.freese.ui.sellermain.sellerorder.SellerOrderFragment
import com.example.freese.ui.sellermain.sellerproduct.SellerProductsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SellerMainActivity : AppCompatActivity() {

   private lateinit var binding: ActivitySellerMainBinding

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivitySellerMainBinding.inflate(layoutInflater)
      setContentView(binding.root)

      if (savedInstanceState == null) {
         // Kita akan buat Fragment ini di langkah selanjutnya
         // replaceFragment(SellerHomeFragment())
      }

      setupBottomNavigation()
   }

   private fun setupBottomNavigation() {
      binding.bottomNavSeller.setOnItemSelectedListener { item ->
         when (item.itemId) {
            R.id.nav_seller_home -> {
               // replaceFragment(SellerHomeFragment())
               true
            }
            R.id.nav_seller_products -> {
                replaceFragment(SellerProductsFragment())
               true
            }
            R.id.nav_seller_orders -> {
                replaceFragment(SellerOrderFragment())
               true
            }
            R.id.nav_seller_profile -> {
               // Anda bisa menggunakan AccountFragment/ProfileFragment yang sama dengan milik pembeli!
               // replaceFragment(AccountFragment())
               true
            }
            else -> false
         }
      }
   }

   private fun replaceFragment(fragment: Fragment) {
      supportFragmentManager.beginTransaction()
         .replace(R.id.fragment_container_seller, fragment)
         .commit()
   }
}