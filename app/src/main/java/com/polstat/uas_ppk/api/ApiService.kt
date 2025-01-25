package com.polstat.uas_ppk.api

import com.polstat.uas_ppk.api.model.AuthRequest
import com.polstat.uas_ppk.api.model.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
}