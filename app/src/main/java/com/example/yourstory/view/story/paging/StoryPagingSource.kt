package com.example.yourstory.view.story.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.yourstory.model.StoryResponseData
import com.example.yourstory.model.api.DicodingAPI

open class StoryPagingSource(
    private val api: DicodingAPI,
    private val Token: String,
    private val maxPages: Int = 50
) : PagingSource<Int, StoryResponseData>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryResponseData> {

        return try {
            val nextPageNumber = params.key ?: 1
            if (nextPageNumber > maxPages) {
                return LoadResult.Page(data = emptyList(), prevKey = nextPageNumber - 1, nextKey = null)
            }
            val response = api.GETAllStories("Bearer " + Token, nextPageNumber, params.loadSize, null)
            println("Bearer " + Token)
//            val responseData = mutableListOf<StoryResponseData>()
//            val data = response.body()?.listStory ?: emptyList()
//            responseData.addAll(data)
//            println("Data: $data") // Debugging line
            Log.d("StoryPagingSource", "load: $response nextPageNumber: $nextPageNumber")

            return LoadResult.Page(
                data = response.body()?.listStory!!,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (response.body()?.listStory!!.isEmpty()) null else nextPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryResponseData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}