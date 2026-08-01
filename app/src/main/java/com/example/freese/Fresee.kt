package com.example.freese

import android.app.Application
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Fresee : Application() {
   override fun onCreate() {
      super.onCreate()

      // Inisialisasi Midtrans
      initMidtransSdk()
   }

   private fun initMidtransSdk() {
      // Ganti "CLIENT_KEY_ANDA" dengan Client Key Sandbox dari Dashboard Midtrans Anda
      val clientKey = "Mid-client-sIn6E_Ij_AMPoDDO"
      val baseUrl = "http://192.168.100.32:3000/" // Base URL Backend Anda

      SdkUIFlowBuilder.init()
         .setClientKey(clientKey)
         .setContext(this)
         .setTransactionFinishedCallback { result ->
            // Ini akan dieksekusi saat user selesai/batal bayar di pop-up
            // Untuk sementara kita biarkan kosong, kita akan handle di UI nanti
         }
         .setMerchantBaseUrl(baseUrl)
         .enableLog(true) // Membantu melihat error di Logcat
         .setColorTheme(
            // Opsional: Menyesuaikan warna Midtrans dengan warna tema aplikasi Anda
            CustomColorTheme("#4CAF50", "#2E7D32", "#81C784")
         )
         .buildSDK()
   }
}