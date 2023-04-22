package com.example.yourstory.view.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.example.yourstory.databinding.AuthActivityBinding
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import com.example.yourstory.view.auth.login.LoginActivity
import com.example.yourstory.view.auth.register.RegisterActivity
import com.example.yourstory.view.story.StoryActivity
import com.example.yourstory.viewmodel.auth.AuthViewModel
import com.example.yourstory.viewmodel.auth.AuthViewModelFactory

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: AuthActivityBinding
    private lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val repository = Repository()
        val sessionManager = SessionManager(this)
        val viewModelFactory = AuthViewModelFactory(repository, sessionManager)
        viewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]

        if (viewModel.checkLoginStatus() == true) {
            val intent = Intent(this, StoryActivity::class.java)
            startActivity(intent)
        }

        Log.d("AuthActivity", "onCreate: ${viewModel.checkLoginStatus()}")

        binding.btLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        animateLogo()
    }

    private fun animateLogo() {
        binding.ivAuth.apply {
            ViewCompat.setTranslationX(this, -100f)
            ViewCompat.setAlpha(this, 0f)
            ViewCompat.animate(this)
                .translationX(0f)
                .alpha(1f)
                .setDuration(500)
                .setStartDelay((100).toLong())
                .start()
        }
    }
}