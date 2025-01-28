package com.polstat.uas_ppk.api.model

data class MyServerResponse(
    val data: List<ServerRequest>,
    val totalPages: Int,
    val page: Int,
    val totalElements: Int,
    val status: String
)

data class ServerRequest(
    val id: Int,
    val username: String,
    val password: String,
    val purpose: String,
    val status: String
)