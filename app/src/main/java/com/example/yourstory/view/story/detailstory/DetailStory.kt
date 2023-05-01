package com.example.yourstory.view.story.detailstory

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.yourstory.R
import com.example.yourstory.databinding.StoryDetailActivityBinding
import com.example.yourstory.model.StoryResponseData

class DetailStory : AppCompatActivity() {

    private lateinit var binding: StoryDetailActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StoryDetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyDetail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("storyDetail", StoryResponseData::class.java)
        } else {
            intent.getParcelableExtra<StoryResponseData>("storyDetail")
        }

        Glide.with(this)
            .load(storyDetail?.photoUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(binding.ivUserData)

        binding.tvNameData.text = storyDetail?.name
        binding.tvDateData.text = storyDetail?.createdAt
        binding.tvDescData.text = storyDetail?.description

        Log.d("DetailStory", "onCreate: $storyDetail")
    }
}