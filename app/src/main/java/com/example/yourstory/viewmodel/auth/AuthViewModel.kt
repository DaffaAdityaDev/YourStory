package com.example.yourstory.viewmodel.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourstory.model.RegisterResponse
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import kotlinx.coroutines.launch
import retrofit2.HttpException

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
