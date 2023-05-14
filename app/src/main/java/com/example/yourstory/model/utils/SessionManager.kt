package com.example.yourstory.model.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "session",
        Context.MODE_PRIVATE
    )

    fun saveAuthToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    fun fetchAuthToken(): String? {
        return sharedPreferences.getString("auth_token", "empty")
    }

    fun clearAuthToken() {
        sharedPreferences.edit().remove("auth_token").apply()
    }

    fun isLoggedIn(): Boolean {
        return fetchAuthToken() != "empty"
    }
}