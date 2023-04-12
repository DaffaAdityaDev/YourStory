package com.example.yourstory.view.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yourstory.R
import com.example.yourstory.databinding.AuthActivityBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: AuthActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}