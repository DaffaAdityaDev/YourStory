package com.example.yourstory.view.story

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.yourstory.R
import com.example.yourstory.databinding.StoryActivityBinding
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import com.example.yourstory.view.auth.AuthActivity
import com.example.yourstory.view.map.MapsActivity
import com.example.yourstory.view.story.addstory.AddStoryActivity
import com.example.yourstory.view.story.recyclerview.adapter.StoryAdapter
import com.example.yourstory.viewmodel.story.StoryViewModel
import com.example.yourstory.viewmodel.story.StoryViewModelFactory

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: StoryActivityBinding
    private lateinit var viewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StoryActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Story"

        val repository = Repository()
        val sessionManager = SessionManager(this)
        val viewModelFactory = StoryViewModelFactory(repository, sessionManager)
        viewModel = ViewModelProvider(this, viewModelFactory)[StoryViewModel::class.java]

        if (viewModel.checkLoginStatus() == false) {
            val intent = Intent(this, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        setupStoryList()

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_upload -> {
                    Log.d("StoryActivity", "Home")
                    val intent = Intent(this, AddStoryActivity::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.action_map -> {
                    Log.d("StoryActivity", "Map")
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }

                else -> false
            }

        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.liststory_menu, menu)

        val logoutItem = menu?.findItem(R.id.action_logout)
        val logoutView = logoutItem?.actionView
        logoutView?.setOnClickListener {
            onOptionsItemSelected(logoutItem)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                Log.d("StoryActivity", "Logout")
                viewModel.clearAuthToken()
                val intent = Intent(this, AuthActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setupStoryList() {
        val adapter = StoryAdapter()
        binding.storyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.storyRecyclerView.adapter = adapter

        viewModel.storiesList.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }
}