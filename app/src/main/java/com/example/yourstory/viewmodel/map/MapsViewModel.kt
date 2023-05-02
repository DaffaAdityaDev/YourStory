package com.example.yourstory.viewmodel.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourstory.model.StoryResponseData
import com.example.yourstory.model.api.RetrofitInstanceBuilder.api
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import kotlinx.coroutines.launch

class MapsViewModel(
    private val repository: Repository,
    private val sessionManager: SessionManager
) : ViewModel() {
    val _storiesList = MutableLiveData<List<StoryResponseData>>()
    val stories: LiveData<List<StoryResponseData>> = _storiesList

    fun getToken() : String? {
        return sessionManager.fetchAuthToken()
    }

    fun fetchAllStories(token: String, location: Int? = null) {
        viewModelScope.launch {
            try {
                val response = api.GETAllStories(token, null, null, location)
                if (response.isSuccessful) {
                    _storiesList.value = response.body()?.listStory
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}