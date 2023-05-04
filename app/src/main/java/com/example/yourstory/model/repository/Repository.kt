package com.example.yourstory.model.repository

import com.example.yourstory.model.*
import com.example.yourstory.model.api.DicodingAPI
import com.example.yourstory.model.api.RetrofitInstanceBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    suspend fun POSTStory(
        token: String,
        description: RequestBody,
        photo: MultipartBody.Part,
        lat: Float? = null,
        lon: Float? = null
    ): Response<GetStoryResponse> {
        return DicodingApi.POSTStory(token, photo, description, lat, lon)
    }

    fun getDicodingAPI(): DicodingAPI {
        return DicodingApi
    }


}


