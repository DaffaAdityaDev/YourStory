package com.example.yourstory.model.repository

import com.example.yourstory.model.RegisterRequest
import com.example.yourstory.model.RegisterResponse
import com.example.yourstory.model.api.RetrofitInstanceBuilder

class Repository {

    suspend fun postRegister(
        name: String,
        email: String,
        password: String
    ) : RegisterResponse {
        return RetrofitInstanceBuilder.api.register(
            RegisterRequest(name, email, password)
        )
    }
}