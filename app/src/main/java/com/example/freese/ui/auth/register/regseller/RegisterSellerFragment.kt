package com.example.freese.ui.auth.register.regseller

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.freese.data.remote.api.request.LoginRequest
import com.example.freese.data.remote.api.request.RegisterRequest
import com.example.freese.databinding.FragmentRegisterSellerBinding
import com.example.freese.viewmodel.AuthViewModel
import com.example.freese.ui.auth.login.LoginActivity
import com.example.freese.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.getValue

// Fragment kosong sementara untuk tab Penjual
@AndroidEntryPoint
class RegisterSellerFragment : Fragment() {
   private var _binding: FragmentRegisterSellerBinding? = null
   private val binding get() = _binding!!

   private val viewModel: AuthViewModel by viewModels()

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentRegisterSellerBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      setupObservers()

      binding.registerButton.setOnClickListener {
         // Siapkan data teks menjadi RequestBody
         val email = binding.edRegisterEmail.text.toString().trim().toRequestBody("text/plain".toMediaTypeOrNull())
         val password = binding.edRegisterPassword.text.toString().trim().toRequestBody("text/plain".toMediaTypeOrNull())
         val fullName = binding.edRegisterName.text.toString().trim().toRequestBody("text/plain".toMediaTypeOrNull())
         val phone = binding.edRegisterNomor.text.toString().trim().toRequestBody("text/plain".toMediaTypeOrNull())

         // Ubah "seller" menjadi "FARMER" (atau "BUYER" tergantung halaman apa ini)
         val role = "FARMER".toRequestBody("text/plain".toMediaTypeOrNull())

         // Panggil viewModel dengan parameter baru (photo diset null dulu jika belum ada fitur upload foto saat register)
         viewModel.registerUser(email, password, fullName, phone, role, null)
      }
   }

   private fun setupObservers() {
      lifecycleScope.launch {
         viewModel.registerState.collect { result ->
            when (result) {
               is Result.Loading -> {
                  binding.progressBar.visibility = View.VISIBLE
               }
               is Result.Success -> {
                  binding.progressBar.visibility = View.GONE
                  Toast.makeText(requireContext(), "Registrasi berhasil! Silakan login.", Toast.LENGTH_LONG).show()
                  activity?.finish() // Tutup RegisterActivity
                  startActivity(Intent(requireContext(), LoginActivity::class.java))
               }
               is Result.Error -> {
                  binding.progressBar.visibility = View.GONE
                  Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
               }
               null -> { /* Initial state */ }
            }
         }
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null // Kosongkan binding saat view dihancurkan
   }
}