package com.example.yourstory.viewmodel.auth.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager

class StoryViewModelFactory(
    private val repository: Repository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            return StoryViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class StoryViewModelFactory")

//        return AuthViewModel(repository) as T
    }
}