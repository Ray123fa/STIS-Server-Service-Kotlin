package com.polstat.uas_ppk.api.model

// Model untuk data Owner (pemilik pengajuan server)
data class Owner(
    val name: String,
    val id: Int,
    val email: String
)

// Model untuk satu permintaan server
data class AllServerRequest(
    val id: Int,
    val owner: Owner,
    val purpose: String,
    val status: String
)

// Model untuk daftar pengajuan server
data class AllServerResponse(
    val data: List<AllServerRequest>,
    val totalPages: Int,
    val page: Int,
    val status: String,
    val totalElements: Int
)
