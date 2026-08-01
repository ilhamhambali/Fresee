package com.example.freese.ui.main.option

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.freese.data.remote.api.response.UserProfileResponse
import com.example.freese.databinding.FragmentOptionBinding
import com.example.freese.ui.main.option.account.AccountActivity
import com.example.freese.ui.main.option.account.FormAccountActivity
import com.example.freese.viewmodel.UserViewModel
import com.example.freese.utils.Result
import com.example.freese.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.jvm.java

@AndroidEntryPoint
class OptionFragment : Fragment() {

   private var _binding: FragmentOptionBinding? = null
   private val binding get() = _binding!!

   private val userViewModel: UserViewModel by viewModels()
   private val authViewModel: AuthViewModel by viewModels()



   // Simpan data profil di variabel ini untuk dilempar ke EditProfileActivity
   private var currentProfile: UserProfileResponse? = null

   override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
      _binding = FragmentOptionBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      setupObservers()

      binding.btnEditProfile.setOnClickListener {
         if (currentProfile != null) {
            val intent = Intent(requireContext(), AccountActivity::class.java)
            // Lempar data saat ini agar form edit tidak kosong
            intent.putExtra("EXTRA_PROFILE", currentProfile)
            startActivity(intent)
         }
      }

      binding.btnChangePassword.setOnClickListener {
         showChangePasswordDialog()
      }

      binding.btnLogout.setOnClickListener {
         authViewModel.logout()
      }
   }

   override fun onResume() {
      super.onResume()
      // Panggil API Get Profile setiap kali halaman ini muncul
      userViewModel.getProfile()
   }

   private fun setupObservers() {
      lifecycleScope.launch {
         userViewModel.profileState.collect { result ->
            when (result) {
               is Result.Loading -> { /* Tampilkan loading */ }
               is Result.Success -> {
                  currentProfile = result.data

                  binding.tvName.text = currentProfile!!.fullName

                  // 1. Ubah "seller" menjadi "FARMER"
                  if (currentProfile!!.role == "FARMER") {
                     binding.tvStoreName.visibility = View.VISIBLE
                     // 2. Karena farmName dihapus, kita tampilkan alamat saja (atau fullName)
                     binding.tvStoreName.text = currentProfile!!.address ?: "Alamat belum diatur"
                  }

                  // 3. Ubah avatar & image menjadi photo
                  if (currentProfile!!.photo != null) {
                     Glide.with(requireContext()).load(currentProfile!!.photo).into(binding.ivProfile)
                  }
               }
               is Result.Error -> { /* Tampilkan error */ }
               null -> {}
            }
         }
      }

      // Observer untuk hasil Ganti Password
      lifecycleScope.launch {
         userViewModel.changePasswordState.collect { result ->
            if (result is Result.Success) {
               Toast.makeText(requireContext(), "Password berhasil diubah!", Toast.LENGTH_SHORT).show()
               userViewModel.resetActionStates()
            } else if (result is Result.Error) {
               Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
               userViewModel.resetActionStates()
            }
         }
      }
   }

   // MEMBUAT DIALOG POP-UP UNTUK GANTI PASSWORD
   private fun showChangePasswordDialog() {
      val layout = LinearLayout(requireContext()).apply {
         orientation = LinearLayout.VERTICAL
         setPadding(50, 40, 50, 10)
      }

      val etOldPass = EditText(requireContext()).apply {
         hint = "Password Lama"
         inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
      }
      val etNewPass = EditText(requireContext()).apply {
         hint = "Password Baru"
         inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
      }

      layout.addView(etOldPass)
      layout.addView(etNewPass)

      AlertDialog.Builder(requireContext())
         .setTitle("Ganti Password")
         .setView(layout)
         .setPositiveButton("Simpan") { dialog, _ ->
            val oldP = etOldPass.text.toString()
            val newP = etNewPass.text.toString()

            if (oldP.isNotEmpty() && newP.isNotEmpty()) {
               userViewModel.changePassword(oldP, newP)
            } else {
               Toast.makeText(requireContext(), "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
         }
         .setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
         .show()
   }
}