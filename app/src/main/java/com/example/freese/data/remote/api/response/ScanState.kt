package com.example.freese.data.remote.api.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScanState(
   val isLoading: Boolean = false,
   val scanResult: ScanResponse? = null,
   val error: String? = null

): Parcelable