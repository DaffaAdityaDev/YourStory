package com.example.yourstory.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
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
data class LoginRequest(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResponseData
)

@Serializable
data class LoginResponseData(
    val userId: String,
    val name: String,
    val token: String
)
@Serializable
data class StoryRequest(
    val error: Boolean,
    val message: String,
    val listStory: List<StoryResponseData>
)
@Serializable
@Parcelize
data class StoryResponseData(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double,
    val lon: Double
) : Parcelable

data class GetStoryResponse(
    val error: Boolean,
    val message: String,
)
