package com.example.yourstory.viewmodel.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager

class MapsViewModelFactorys(
    private val repository: Repository,
    private val sessionManager: SessionManager)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}