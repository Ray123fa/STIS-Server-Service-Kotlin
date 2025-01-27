package com.polstat.uas_ppk.api.model

data class UserListResponse(
    val data: List<User>,
    val totalPages: Int,
    val page: Int,
    val totalElements: Int,
    val status: String
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val role: String,
)