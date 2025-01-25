package com.polstat.uas_ppk.api.model

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)
