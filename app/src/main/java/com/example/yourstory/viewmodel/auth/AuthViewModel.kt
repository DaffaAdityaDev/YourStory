package com.example.yourstory.viewmodel.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yourstory.model.RegisterResponse
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager

class AuthViewModel(
    private val repository: Repository,
    private val sessionManager: SessionManager
    ) : ViewModel() {
    val _postResponse: MutableLiveData<RegisterResponse> = MutableLiveData()

    fun checkLoginStatus(): Boolean {
        return sessionManager.isLoggedIn()
    }

    fun getAuthToken(): String? {
        return sessionManager.fetchAuthToken()
    }

}
