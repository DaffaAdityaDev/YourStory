package com.example.yourstory.model.repository

import com.example.yourstory.model.LoginRequest
import com.example.yourstory.model.RegisterRequest
import com.example.yourstory.model.RegisterResponse
import com.example.yourstory.model.StoryRequest
import com.example.yourstory.model.api.RetrofitInstanceBuilder
import retrofit2.Response

class Repository {
    private val DicodingApi = RetrofitInstanceBuilder.api

    suspend fun POSTRegister(
        name: String,
        email: String,
        password: String
    ): Response<RegisterResponse> {
        return DicodingApi.POSTRegister(
            RegisterRequest(name, email, password)
        )
    }

    suspend fun POSTLogin(email: String, password: String): Response<LoginRequest> {
        return DicodingApi.POSTLogin(mapOf("email" to email, "password" to password))
    }

    suspend fun getAllStories(
        token: String,
        page: Int? = null,
        size: Int? = null,
        location: Int? = null
    ): Response<StoryRequest> {
        return DicodingApi.GETAllStories(token, page, size, location)
    }
}
