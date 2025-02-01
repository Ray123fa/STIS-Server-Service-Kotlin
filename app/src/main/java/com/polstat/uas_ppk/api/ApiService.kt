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

    @POST("api/server/request")
    suspend fun requestServer(
        @Header("Authorization") token: String, // Bearer Token
        @Body purpose: MyServerRequest
    ): Response<Map<String, Any>>

    @GET("api/server/my-requests")
    suspend fun getMyServerRequests(
        @Header("Authorization") token: String
    ): Response<MyServerResponse>

    @GET("api/server/requests")
    suspend fun getAllServerRequests(
        @Header("Authorization") token: String
    ): Response<AllServerResponse>

    @PATCH("api/server/request/{id}/approve")
    suspend fun approveServerRequest(
        @Header("Authorization") token: String,
        @Path("id") requestId: Int
    ): Response<ApproveResponse>

    @PATCH("api/server/request/{id}/reject")
    suspend fun rejectServerRequest(
        @Header("Authorization") token: String,
        @Path("id") requestId: Int,
        @Body reason: RejectRequest
    ): Response<Map<String, Any>>
}