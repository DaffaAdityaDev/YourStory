package com.example.yourstory.viewmodel.auth.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourstory.model.LoginRequest
import com.example.yourstory.model.LoginResponseData
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class LoginViewModel(
    private val repository: Repository,
    private val sessionManager: SessionManager
    ) : ViewModel() {
    var _postResponse: MutableLiveData<LoginRequest> = MutableLiveData()

    fun checkLoginStatus(): Boolean {
        return sessionManager.isLoggedIn()
    }


    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _postResponse.value = LoginRequest(
                    false,
                    "Loading...",
                    LoginResponseData("", "", "")
                )

                val response = repository.POSTLogin(email, password)
                if (response.isSuccessful) {
                    _postResponse.value = response.body()
                    sessionManager.saveAuthToken(response.body()?.loginResult?.token.toString())
                    // Handle successful login
                    Log.d("LoginViewModel", "Login successful: ${response.body()}")
                } else {
                    // Handle unsuccessful login
                    val errorBody = response.errorBody()?.string()
                    val jsonObject = JSONObject(errorBody)
                    val message = jsonObject.getString("message")
                    Log.d("LoginViewModel", "Login failed: $message")
                    _postResponse.value = LoginRequest(
                        true,
                        message,
                        LoginResponseData("", "", ""))
                }
            } catch (e: HttpException) {
                // Handle HTTP error
                Log.e("LoginViewModel", "HTTP error: \${e.message}")
                _postResponse.value = LoginRequest(
                    true,
                    e.message(),
                    LoginResponseData("", "", ""))
            } catch (e: Exception) {
                // Handle other exceptions
                Log.e("LoginViewModel", "Exception: \${e.message}")
            }
        }
    }
}