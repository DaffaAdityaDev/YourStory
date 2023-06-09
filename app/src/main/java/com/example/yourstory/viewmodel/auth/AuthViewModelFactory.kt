package com.example.yourstory.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager

class AuthViewModelFactory(
    private val repository: Repository,
    private val sessionManager: SessionManager
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class AuthViewModelFactory")

//        return AuthViewModel(repository) as T
    }
}