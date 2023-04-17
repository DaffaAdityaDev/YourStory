package com.example.yourstory.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourstory.databinding.AuthActivityBinding
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import com.example.yourstory.view.auth.login.LoginActivity
import com.example.yourstory.view.auth.register.RegisterActivity
import com.example.yourstory.view.story.StoryActivity
import com.example.yourstory.viewmodel.auth.AuthViewModel
import com.example.yourstory.viewmodel.auth.AuthViewModelFactory
import retrofit2.HttpException

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
//        viewModel.postRegister("udin", "udin22@gmail.com", "hatihati")
//        viewModel._postResponse.observe(this) { response ->
//            binding.tvAuthHello.text = response.message + "fuck you"
//            Log.d("AuthActivity", "onCreate: ${response.message}")
//            if (response.error) {
//                Log.d("AuthActivity", "onCreate: ${response.message}")
//                binding.tvAuthHello.text = response.message + "fuck you"
//            } else {
//                Log.d("AuthActivity", "onCreate: ${response.message}")
//                binding.tvAuthHello.text = response.message + "fuck me"
//            }
//        }


        binding.btLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}