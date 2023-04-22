package com.example.yourstory.view.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.example.yourstory.databinding.AuthRegisterActivityBinding
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.view.auth.login.LoginActivity
import com.example.yourstory.viewmodel.auth.register.RegisterViewModel
import com.example.yourstory.viewmodel.auth.register.RegisterViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: AuthRegisterActivityBinding
    private lateinit var viewModel: RegisterViewModel

    companion object {
        const val TAG = "RegisterActivity"
        const val VALIDEMAIL = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthRegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val repository = Repository()
        viewModel = RegisterViewModel(repository)
        viewModel = ViewModelProvider(this, RegisterViewModelFactory(repository))[RegisterViewModel::class.java]

        viewModel._postResponse.observe(this) { response ->
            if (!response.error) {
                // Handle successful response
                Log.d("RegisterActivity", "Success: ${response.message}")

                binding.tvStatusRegister.visibility = android.view.View.VISIBLE
                binding.tvStatusRegister.text = response.message
                binding.tvStatusRegister.setTextColor(ContextCompat.getColor(applicationContext, android.R.color.holo_green_dark))
            } else {
                // Handle error response
                binding.tvStatusRegister.visibility = android.view.View.VISIBLE
                binding.tvStatusRegister.text = response.message
                binding.tvStatusRegister.setTextColor(ContextCompat.getColor(applicationContext, android.R.color.holo_red_dark))
                Log.d("RegisterActivity", "Error: ${response.message}")
            }
        }

        binding.btRegisterPage.setOnClickListener {
            val name = binding.edNameInput.text.toString()
            val email = binding.edEmailInput.text.toString()
            val password = binding.edPasswordInput.text.toString()
            onRegisterButtonClick(name, email, password)
        }

        binding.tvRegister.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.tvLoginBlue.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun onRegisterButtonClick(name: String, email: String, password: String) {
        if (!checkFieldsIsNotEmpty(name, email, password)) {
            Toast.makeText(applicationContext, "Tolong isi Field", Toast.LENGTH_SHORT).show()
            return
        }
        if (!checkEmailAndPasswordIsValid(email, password)) {
            Toast.makeText(applicationContext, "Tolong masukan email and password yang benar", Toast.LENGTH_SHORT).show()
            return
        }
        viewModel.postRegister(name, email, password)
    }

    private fun checkFieldsIsNotEmpty(name: String, email: String, password: String): Boolean {
        return name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()
    }

    private fun checkEmailAndPasswordIsValid(email: String, password: String): Boolean {
        return email.matches(VALIDEMAIL.toRegex()) && password.length >= 8
    }

    override fun onResume() {
        super.onResume()
        animateLogo()
    }

    private fun animateLogo() {
        binding.imRegister.apply {
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