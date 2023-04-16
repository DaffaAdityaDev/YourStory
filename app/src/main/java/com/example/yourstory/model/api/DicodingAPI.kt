package com.example.yourstory.model.api

import com.example.yourstory.model.LoginResponse
import com.example.yourstory.model.RegisterRequest
import com.example.yourstory.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DicodingAPI {
    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("login")
    suspend fun login(
        @Body loginRequest: Map<String, String>
    ): Response<LoginResponse>
}

