package com.example.freese.ui.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DomisiliPickerBottomSheet(
   // Callback untuk mengirim data balik ke XML Fragment/Activity
   private val onLocationSelected: (String, String) -> Unit
) : BottomSheetDialogFragment() {

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
   ): View {
      return ComposeView(requireContext()).apply {
         setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
         setContent {
            MaterialTheme {
               CascadingDomisiliPicker(
                  onDismiss = { dismiss() },
                  onComplete = { alamatLengkap, kodePos ->
                     onLocationSelected(alamatLengkap, kodePos)
                     dismiss()
                  }
               )
            }
         }
      }
   }
}

@Composable
fun CascadingDomisiliPicker(onDismiss: () -> Unit, onComplete: (String, String) -> Unit) {
   // State Management untuk Step Picker
   var currentStep by remember { mutableStateOf(0) } // 0: Prov, 1: Kota, 2: Kec
   var selectedProvinsi by remember { mutableStateOf("") }
   var selectedKota by remember { mutableStateOf("") }

   // --- Mock Data (Nanti bisa kamu ganti dari API/Room Database) ---
   val listProvinsi = listOf("Jawa Barat", "Jawa Tengah", "Jawa Timur")
   val listKotaJabar = listOf("Kota Bandung", "Kab. Bandung", "Kota Cimahi")
   val listKecamatanKabBandung = listOf(
      Pair("Katapang", "40921"),
      Pair("Margahayu", "40226"),
      Pair("Soreang", "40911")
   )
   // ----------------------------------------------------------------

   Column(
      modifier = Modifier
         .fillMaxWidth()
         .fillMaxHeight(0.7f) // Tinggi bottom sheet 70% layar
         .padding(16.dp)
   ) {
      // Header & Breadcrumb
      Text("Pilih Lokasi", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
      Spacer(modifier = Modifier.height(8.dp))

      // Menampilkan lokasi yang sedang dipilih sebagai navigasi mundur
      if (selectedProvinsi.isNotEmpty()) {
         Text(
            text = selectedProvinsi,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { currentStep = 0; selectedProvinsi = ""; selectedKota = "" }
         )
      }
      if (selectedKota.isNotEmpty()) {
         Text(
            text = "> $selectedKota",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { currentStep = 1; selectedKota = "" }
         )
      }

      Divider(modifier = Modifier.padding(vertical = 12.dp))

      // List Data Dinamis
      LazyColumn {
         when (currentStep) {
            0 -> {
               items(listProvinsi) { prov ->
                  Text(
                     text = prov,
                     modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                           selectedProvinsi = prov
                           currentStep = 1 // Pindah ke list Kota
                        }
                        .padding(vertical = 12.dp)
                  )
               }
            }
            1 -> {
               // Logika sederhana untuk milih list kota (sebaiknya pakai ID nantinya)
               val kotaToShow = if (selectedProvinsi == "Jawa Barat") listKotaJabar else emptyList()
               items(kotaToShow) { kota ->
                  Text(
                     text = kota,
                     modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                           selectedKota = kota
                           currentStep = 2 // Pindah ke list Kecamatan
                        }
                        .padding(vertical = 12.dp)
                  )
               }
            }
            2 -> {
               // Menampilkan list Kecamatan beserta kode pos
               val kecToShow = if (selectedKota == "Kab. Bandung") listKecamatanKabBandung else emptyList()
               items(kecToShow) { (kecamatan, kodePos) ->
                  Text(
                     text = kecamatan,
                     modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                           // Selesai! Gabungkan text dan kirim ke callback
                           val alamatLengkap = "$selectedProvinsi, $selectedKota, $kecamatan"
                           onComplete(alamatLengkap, kodePos)
                        }
                        .padding(vertical = 12.dp)
                  )
               }
            }
         }
      }
   }
}