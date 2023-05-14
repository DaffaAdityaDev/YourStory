package com.example.yourstory.viewmodel.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.yourstory.model.StoryResponseData
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager

class StoryViewModel(
    val repository: Repository,
    var sessionManager: SessionManager,
) : ViewModel() {

    val storiesList: LiveData<PagingData<StoryResponseData>> by lazy {
        repository.getStoryPaging(repository, sessionManager).cachedIn(viewModelScope)
    }

    fun checkLoginStatus(): Boolean {
        return sessionManager.isLoggedIn()
    }

    fun clearAuthToken() {
        sessionManager.clearAuthToken()
    }

    fun getToken() : String? {
       return sessionManager.fetchAuthToken()
    }

}
