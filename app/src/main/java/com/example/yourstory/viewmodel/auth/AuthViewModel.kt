package com.example.yourstory.viewmodel.auth

import androidx.lifecycle.ViewModel
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager

class AuthViewModel(
    private val repository: Repository,
    private val sessionManager: SessionManager
    ) : ViewModel() {

    fun checkLoginStatus(): Boolean {
        return sessionManager.isLoggedIn()
    }

    fun getAuthToken(): String? {
        return sessionManager.fetchAuthToken()
    }

}
