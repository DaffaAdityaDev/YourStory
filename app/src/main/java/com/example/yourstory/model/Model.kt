package com.example.yourstory.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class RegisterResponse(
    val error: Boolean,
    val message: String
)

@Serializable
data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult
)

@Serializable
data class LoginResult(
    val userId: String,
    val name: String,
    val token: String
)
