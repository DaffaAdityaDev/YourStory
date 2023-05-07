package com.example.yourstory.view.story.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.yourstory.model.StoryResponseData
import com.example.yourstory.model.api.DicodingAPI

class StoryPagingSource(private val api: DicodingAPI, private val Token: String) : PagingSource<Int, StoryResponseData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponseData> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = api.GETAllStories("Bearer " + Token, nextPageNumber, params.loadSize, null)
            println("Bearer " + Token)
            val responseData = mutableListOf<StoryResponseData>()
            val data = response.body()?.listStory ?: emptyList()
            responseData.addAll(data)
            Log.d("StoryPagingSource", "load: $response")
            LoadResult.Page(
                data = responseData,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (data.isEmpty()) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryResponseData>): Int? {
        return state.anchorPosition
    }
}