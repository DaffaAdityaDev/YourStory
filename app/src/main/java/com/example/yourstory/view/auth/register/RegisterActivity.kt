package com.example.yourstory.view.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
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
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AuthRegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val repository = Repository()
        viewModel = RegisterViewModel(repository)
        viewModel = ViewModelProvider(this, RegisterViewModelFactory(repository))[RegisterViewModel::class.java]

        binding.edEmailInput.setText("udin22@gmail.com")
        binding.edNameInput.setText("udin")
        binding.edPasswordInput.setText("hatihati")

        binding.btRegisterPage.setOnClickListener {
            val name = binding.edNameInput.text.toString()
            val email = binding.edEmailInput.text.toString()
            val password = binding.edPasswordInput.text.toString()

            Log.d(TAG, name + email + password )

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.postRegister(name, email, password)

                // Observe the API response and update the UI accordingly
                viewModel._postResponse.observe(this) { response ->
                    Log.d(TAG, response.message)
                    Toast.makeText(applicationContext, response.message + " - main", Toast.LENGTH_SHORT).show()
                    if (response.error) {
                        Log.d(TAG, "onCreate: \${response.message}")
                        Toast.makeText(applicationContext, response.message + " - error", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d(TAG, "onCreate: \${response.message}")
                        binding.tvRegister.text = response.message + " - Success"
                        Toast.makeText(applicationContext, response.message + " - Success", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        binding.tvRegister.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.tvRegisterBlue.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }
}