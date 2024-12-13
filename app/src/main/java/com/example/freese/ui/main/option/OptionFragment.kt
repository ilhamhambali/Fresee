package com.example.freese.ui.main.option

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.freese.DependencyProvider
import com.example.freese.GenericViewModelFactory
import com.example.freese.R
import com.example.freese.data.pref.UserPreference
import com.example.freese.data.repository.UserRepository
import com.example.freese.databinding.FragmentOptionBinding
import com.example.freese.ui.auth.AuthViewModel
import com.example.freese.ui.auth.AuthViewModelFactory
import com.example.freese.ui.auth.login.LoginActivity
import com.example.freese.ui.main.option.account.AccountActivity
import com.example.freese.ui.main.option.myproduct.MyProductActivity
import com.example.freese.ui.main.option.sell.SellActivity

class OptionFragment : Fragment() {

   private var _binding: FragmentOptionBinding? = null
   private val binding get() = _binding!!

   private lateinit var authViewModel: AuthViewModel

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentOptionBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      authViewModel = ViewModelProvider(
         this,
         GenericViewModelFactory { AuthViewModel(DependencyProvider.provideUserRepository(requireContext())) }
      )[AuthViewModel::class.java]

      // Logout Button
      binding.logoutButton.setOnClickListener {
         authViewModel.logout()
         val intent = Intent(requireContext(), LoginActivity::class.java)
         startActivity(intent)
      }

      intentActivity()
   }

   private fun intentActivity() {
      // Account option
      binding.accountCard.setOnClickListener {
         val intent = Intent(requireContext(), AccountActivity::class.java)
         startActivity(intent)
      }

      // My Products option
      binding.myProductsCard.setOnClickListener {
         val intent = Intent(requireContext(), MyProductActivity::class.java)
         startActivity(intent)
      }

      // Sell Product option
      binding.sellProductCard.setOnClickListener {
         val intent = Intent(requireContext(), SellActivity::class.java)
         startActivity(intent)
      }

      // Help option
//      binding.helpCard.setOnClickListener {
//         val intent = Intent(requireContext(), HelpActivity::class.java)
//         startActivity(intent)
//      }
//
//      // About option
//      binding.aboutCard.setOnClickListener {
//         val intent = Intent(requireContext(), AboutActivity::class.java)
//         startActivity(intent)
//      }
   }
   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}
