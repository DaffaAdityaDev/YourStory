package com.example.yourstory.model.api

import com.example.yourstory.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @Multipart
    @POST("stories")
    suspend fun POSTStory(
        @Header("Authorization") token: String,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float?,
        @Part("lon") lon: Float?
    ) : Response<GetStoryResponse>

}

