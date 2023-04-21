package com.example.yourstory.viewmodel.story.addstory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager

class AddStoryViewModelFactory(
    private val repository: Repository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class AddStoryViewModelFactory")
    }
}