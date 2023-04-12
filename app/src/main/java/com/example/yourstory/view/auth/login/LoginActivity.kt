package com.example.yourstory.view.auth.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yourstory.R
import com.example.yourstory.databinding.AuthLoginActivityBinding
import com.example.yourstory.viewmodel.auth.login.LoginActivityViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginActivityViewModel
    private lateinit var binding: AuthLoginActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_login_activity)

        actionBar?.title = "Login"
        actionBar?.hide()

        viewModel = LoginActivityViewModel()

//        binding.imageView.setImageResource(R.drawable.ic_launcher_background)

    }
}