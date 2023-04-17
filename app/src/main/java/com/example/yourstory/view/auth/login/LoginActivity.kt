package com.example.yourstory.view.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.yourstory.databinding.AuthLoginActivityBinding
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import com.example.yourstory.view.auth.register.RegisterActivity
import com.example.yourstory.view.story.StoryActivity
import com.example.yourstory.viewmodel.auth.login.LoginViewModel
import com.example.yourstory.viewmodel.auth.login.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: AuthLoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthLoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.etEmail.setText("udin22@gmail.com")
        binding.etPassword.setText("hatihati")

        val repository = Repository()
        val sessionManager = SessionManager(this)
        viewModel = LoginViewModel(repository, sessionManager)
        viewModel = ViewModelProvider(this, LoginViewModelFactory(repository, sessionManager))[LoginViewModel::class.java]
        viewModel._postResponse.observe(this) { response ->
            if (!response.error) {
                // Handle successful response
                Log.d("LoginActivity", "Success: ${response.message}")

                binding.tvStatusLogin.visibility = android.view.View.VISIBLE
                binding.tvStatusLogin.text = response.message
                binding.tvStatusLogin.setTextColor(ContextCompat.getColor(applicationContext, android.R.color.holo_green_dark))

                if (response.message == "success") {
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this, StoryActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finish()
                    }, 1000)
                }
            } else {
                // Handle error response
                Log.d("LoginActivity", "Error: ${response.message}")

                binding.tvStatusLogin.visibility = android.view.View.VISIBLE
                binding.tvStatusLogin.text = response.message
                binding.tvStatusLogin.setTextColor(ContextCompat.getColor(applicationContext, android.R.color.holo_red_dark))
            }
        }

        Log.d("LoginActivity", "Login Status: ${viewModel.checkLoginStatus().toString()}")
//        if (viewModel.checkLoginStatus() == true) {
//            viewModel.clearAuthToken()
//        }
//        Log.d("LoginActivity", "Login Status: ${viewModel.checkLoginStatus().toString()}")

        binding.btLoginPage.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            Log.d("", "Email: $email")
            viewModel.login(email, password)
        }

        binding.llRegisterRedirect.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

//        binding.imageView.setImageResource(R.drawable.ic_launcher_background)

    }
}