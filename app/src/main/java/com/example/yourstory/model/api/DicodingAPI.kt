package com.example.yourstory.model.api

import com.example.yourstory.model.LoginRequest
import com.example.yourstory.model.RegisterRequest
import com.example.yourstory.model.RegisterResponse
import com.example.yourstory.model.StoryRequest
import retrofit2.Response
import retrofit2.http.*

interface DicodingAPI {
    @POST("register")
    suspend fun POSTRegister(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("login")
    suspend fun POSTLogin(
        @Body loginRequest: Map<String, String>
    ): Response<LoginRequest>

    @GET("stories")
    suspend fun GETAllStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?
    ) : Response<StoryRequest>
}

