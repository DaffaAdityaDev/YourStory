package com.example.yourstory.view.auth.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yourstory.R
import com.example.yourstory.databinding.AuthRegisterActivityBinding
import com.example.yourstory.viewmodel.auth.register.RegisterActivityViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: AuthRegisterActivityBinding
    private lateinit var viewModel: RegisterActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthRegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = RegisterActivityViewModel()


    }
}