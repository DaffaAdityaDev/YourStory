package com.example.yourstory.viewmodel.auth.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourstory.model.LoginResponse
import com.example.yourstory.model.LoginResult
import com.example.yourstory.model.repository.Repository
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class LoginViewModel(private val repository: Repository) : ViewModel() {
    var _postResponse: MutableLiveData<LoginResponse> = MutableLiveData()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _postResponse.value = LoginResponse(
                    false,
                    "Loading...",
                    LoginResult("", "", "")
                )

                val response = repository.login(email, password)
                if (response.isSuccessful) {
                    _postResponse.value = response.body()
                    // Handle successful login
                    Log.d("LoginViewModel", "Login successful: ${response.body()}")
                } else {
                    // Handle unsuccessful login
                    val errorBody = response.errorBody()?.string()
                    val jsonObject = JSONObject(errorBody)
                    val message = jsonObject.getString("message")
                    Log.d("LoginViewModel", "Login failed: $message")
                    _postResponse.value = LoginResponse(
                        true,
                        message,
                        LoginResult("", "", ""))
                }
            } catch (e: HttpException) {
                // Handle HTTP error
                Log.e("LoginViewModel", "HTTP error: \${e.message}")
                _postResponse.value = LoginResponse(
                    true,
                    e.message(),
                    LoginResult("", "", ""))
            } catch (e: Exception) {
                // Handle other exceptions
                Log.e("LoginViewModel", "Exception: \${e.message}")
            }
        }
    }
}