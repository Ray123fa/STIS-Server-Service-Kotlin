package com.polstat.uas_ppk.api.model

data class AuthResponse(
    val name: String,
    val email: String,
    val role: String,
    val accessToken: String
)