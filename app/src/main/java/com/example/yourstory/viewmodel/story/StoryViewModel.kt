package com.example.yourstory.viewmodel.story

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.yourstory.model.StoryResponseData
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import com.example.yourstory.view.story.paging.StoryPagingSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class StoryViewModel(
    val repository: Repository,
    val sessionManager: SessionManager,
    private val coroutineScope: CoroutineScope? = null
) : ViewModel() {
    val _storiesList = MutableLiveData<List<StoryResponseData>>()

    var storyPagingFlow: Flow<PagingData<StoryResponseData>> = Pager(
        config = PagingConfig(
            pageSize = 15
        ),
        pagingSourceFactory = {

            StoryPagingSource(
                repository.getDicodingAPI(),
                sessionManager.fetchAuthToken() as String ?: ""
            )
        }
    ).flow.cachedIn(viewModelScope)

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
