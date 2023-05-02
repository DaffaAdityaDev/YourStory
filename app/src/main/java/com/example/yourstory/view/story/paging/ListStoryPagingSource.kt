package com.example.yourstory.view.story.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.yourstory.model.StoryRequest
import com.example.yourstory.model.api.DicodingAPI

class ListStoryPagingSource(private val apiService: DicodingAPI) : PagingSource<Int, StoryRequest>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override fun getRefreshKey(state: PagingState<Int, StoryRequest>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryRequest> {
        TODO("Not yet implemented")
    }

}
