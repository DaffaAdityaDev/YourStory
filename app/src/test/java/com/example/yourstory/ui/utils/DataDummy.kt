package com.example.yourstory.ui.utils

import com.example.yourstory.model.GetStoryResponse
import com.example.yourstory.model.StoryRequest
import com.example.yourstory.model.StoryResponseData

object DataDummy {

    fun generateDummyStories(): StoryRequest {
        val listStory = ArrayList<StoryResponseData>()
        for (i in 1..20) {
            val story = StoryResponseData(
                createdAt = "2022-02-22T22:22:22Z",
                description = "Description $i",
                id = "id_$i",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 10,
                name = "Name $i",
                photoUrl = "https://akcdn.detik.net.id/visual/2020/02/14/066810fd-b6a9-451d-a7ff-11876abf22e2_169.jpeg?w=650"
            )
            listStory.add(story)
        }

        return StoryRequest(
            error = false,
            message = "Stories fetched successfully",
            listStory = listStory
        )
    }

    fun generateDummyCreateStory(): GetStoryResponse {
        return GetStoryResponse(
            error = false,
            message = "success"
        )
    }
}