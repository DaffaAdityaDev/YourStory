package com.example.yourstory.model.api

import com.example.yourstory.model.utils.BaseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceBuilder {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BaseUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: DicodingAPI by lazy {
        retrofit.create(DicodingAPI::class.java)
    }
}
