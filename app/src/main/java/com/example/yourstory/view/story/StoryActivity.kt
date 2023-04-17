package com.example.yourstory.view.story

import android.content.Intent
import android.content.SharedPreferences
import android.media.session.MediaSession.Token
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourstory.R
import com.example.yourstory.databinding.StoryActivityBinding
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import com.example.yourstory.view.auth.AuthActivity
import com.example.yourstory.view.auth.login.LoginActivity
import com.example.yourstory.viewmodel.auth.AuthViewModelFactory
import com.example.yourstory.viewmodel.auth.story.StoryViewModel
import com.example.yourstory.viewmodel.auth.story.StoryViewModelFactory

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: StoryActivityBinding
    private lateinit var viewModel: StoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StoryActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = Repository()
        val sessionManager = SessionManager(this)
        val viewModelFactory = StoryViewModelFactory(repository, sessionManager)
        viewModel = ViewModelProvider(this, viewModelFactory)[StoryViewModel::class.java]

        val token = viewModel.getToken() ?: ""
        Log.d("StoryActivity", "Token: $token")
        viewModel.GETStoriesList("Bearer " + token)

        viewModel._storiesList.observe(this) { stories ->
            Log.d("StoryActivity", "Stories: $stories + $token")
        }

        if (viewModel.checkLoginStatus() == false) {
            val intent = Intent(this, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
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
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}