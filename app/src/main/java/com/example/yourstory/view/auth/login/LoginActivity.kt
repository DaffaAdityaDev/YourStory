package com.example.yourstory.view.auth.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.yourstory.databinding.AuthLoginActivityBinding
import com.example.yourstory.viewmodel.auth.login.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: AuthLoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthLoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = LoginViewModel()

        binding.btLoginPage.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            Log.d("TAG222", "onCreate: $email $password")
        }

//        binding.imageView.setImageResource(R.drawable.ic_launcher_background)

    }
}