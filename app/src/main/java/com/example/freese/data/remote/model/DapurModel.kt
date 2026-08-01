package com.example.freese.data.remote.model

data class InventarisBuah(
   val id: Int,
   val namaBuah: String,
   val tanggalMasuk: String,
   val statusKesegaran: String,
   val userId: Int
)

data class TambahBuahRequest(
   val namaBuah: String,
   val statusKesegaran: String
)
