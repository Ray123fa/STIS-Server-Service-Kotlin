package com.polstat.uas_ppk.api.model

data class UpdatePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmPassword: String
)
