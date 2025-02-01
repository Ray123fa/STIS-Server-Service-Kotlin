package com.polstat.uas_ppk.api.model

data class ApproveResponse(
    val message: String,
    val account: Account?,
    val status: String
)

data class Account(
    val username: String,
    val password: String
)