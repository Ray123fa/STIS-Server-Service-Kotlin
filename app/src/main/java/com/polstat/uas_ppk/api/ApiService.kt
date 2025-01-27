package com.polstat.uas_ppk.api

import com.polstat.uas_ppk.api.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): Response<Map<String, Any>>

    @PATCH("api/users/update-password")
    suspend fun updatePassword(
        @Header("Authorization") token: String, // Bearer Token
        @Body request: UpdatePasswordRequest
    ): Response<Map<String, Any>>

    @PATCH("api/users/update-email")
    suspend fun updateEmail(
        @Header("Authorization") token: String, // Bearer Token
        @Body request: UpdateEmailRequest
    ): Response<Map<String, Any>>

    @GET("api/admin/list-user")
    suspend fun getUserList(
        @Header("Authorization") token: String
    ): Response<UserListResponse>

    @DELETE("api/admin/delete-user")
    suspend fun deleteUser(
        @Header("Authorization") token: String, // Bearer Token
        @Query("email") email: String
    ): Response<Map<String, Any>>
}