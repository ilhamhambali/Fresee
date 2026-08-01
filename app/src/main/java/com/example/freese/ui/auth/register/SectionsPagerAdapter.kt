package com.example.freese.ui.auth.register

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.freese.ui.auth.register.regseller.RegisterSellerFragment
import com.example.freese.ui.auth.register.reguser.RegisterUserFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

   override fun getItemCount(): Int {
      return 2 // Jumlah tab
   }

   override fun createFragment(position: Int): Fragment {
      var fragment: Fragment? = null
      when (position) {
         0 -> fragment = RegisterSellerFragment() // Tab 1
         1 -> fragment = RegisterUserFragment()   // Tab 2
      }
      return fragment as Fragment
   }
}
