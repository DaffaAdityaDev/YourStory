package com.example.yourstory.viewmodel.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.yourstory.model.StoryResponseData
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import com.example.yourstory.view.story.paging.StoryPagingSource
import kotlinx.coroutines.launch

class StoryViewModel(
    val repository: Repository,
    var sessionManager: SessionManager,
) : ViewModel() {
    val _storiesList = MutableLiveData<List<StoryResponseData>>()

    val storiesList: LiveData<PagingData<StoryResponseData>> = getStoryPaging().cachedIn(viewModelScope)



    fun getStoryPaging(): LiveData<PagingData<StoryResponseData>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {

                StoryPagingSource(
                    repository.getDicodingAPI(),
                    sessionManager.fetchAuthToken() as String ?: "",
                    maxPages = 20
                )
            }
        ).liveData
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

    fun GETStoriesList(token: String, page: Int? = null, size: Int? = null, location: Int? = null) {
        viewModelScope.launch {
            val response = repository.getAllStories(token, page, size, location)
            _storiesList.value = response.body()?.listStory
        }
    }

}
