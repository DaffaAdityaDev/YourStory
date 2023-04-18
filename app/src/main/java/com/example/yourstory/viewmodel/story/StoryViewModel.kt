package com.example.yourstory.viewmodel.story

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourstory.model.StoryResponseData
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import kotlinx.coroutines.launch

class StoryViewModel(
    val repository: Repository,
    val sessionManager: SessionManager
) : ViewModel() {
    val _storiesList = MutableLiveData<List<StoryResponseData>>()

    fun checkLoginStatus(): Boolean {
        return sessionManager.isLoggedIn()
    }

    fun clearAuthToken() {
        sessionManager.clearAuthToken()
    }

    fun getToken() : String? {
       return sessionManager.fetchAuthToken()
    }

    fun GETStoriesList(token: String, page: Int? = null, size: Int? = null, location: Int? = null) {
        viewModelScope.launch {
            val response = repository.getAllStories(token, page, size, location)
            _storiesList.value = response.body()?.listStory
        }
    }

}