package com.example.yourstory.model.repository

import com.example.yourstory.model.LoginResponse
import com.example.yourstory.model.RegisterRequest
import com.example.yourstory.model.RegisterResponse
import com.example.yourstory.model.api.RetrofitInstanceBuilder
import retrofit2.Response

class Repository {

    private val DicodingApi = RetrofitInstanceBuilder.api
    suspend fun postRegister(
        name: String,
        email: String,
        password: String
    ): Response<RegisterResponse> {
        return DicodingApi.register(
            RegisterRequest(name, email, password)
        )
    }

    suspend fun login(email: String, password: String): Response<LoginResponse> {
        return DicodingApi.login(mapOf("email" to email, "password" to password))
    }

}
