package com.example.yourstory.view.story.recyclerview.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.yourstory.R
import com.example.yourstory.model.StoryResponseData
import com.example.yourstory.view.story.detailstory.DetailStory

class StoryAdapter(
    private val storyList: List<StoryResponseData>,
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    class StoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.story_name)
        val description: TextView = itemView.findViewById(R.id.story_description)
        val photo: ImageView = itemView.findViewById(R.id.story_photo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.story_item, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val currentItem = storyList[position]
        holder.name.text = currentItem.name
        holder.description.text = currentItem.description

        Glide.with(holder.photo.context)
            .load(currentItem.photoUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.photo)

        holder.itemView.setOnClickListener {
            val user = StoryResponseData(
                currentItem.id,
                currentItem.name,
                currentItem.description,
                currentItem.photoUrl,
                currentItem.createdAt,
                currentItem.lat,
                currentItem.lon)
            val intent = Intent(holder.itemView.context, DetailStory::class.java)
            intent.putExtra("storyDetail", user)
            holder.itemView.context.startActivity(intent)
        }

        // Apply the animation
        holder.itemView.apply {
            ViewCompat.setTranslationX(this, -100f)
            ViewCompat.setAlpha(this, 0f)
            ViewCompat.animate(this)
                .translationX(0f)
                .alpha(1f)
                .setDuration(500)
                .setStartDelay((position * 100).toLong())
                .start()
        }

    }

    override fun getItemCount(): Int {
        return storyList.size
    }
}
