package com.example.yourstory.viewmodel.auth.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourstory.model.RegisterResponse
import com.example.yourstory.model.repository.Repository
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    var _postResponse: MutableLiveData<RegisterResponse> = MutableLiveData()

    fun postRegister(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _postResponse.value = RegisterResponse(false, "Loading...")
                val response = repository.POSTRegister(name, email, password)
                if (response.isSuccessful) {
                    // Handle successful response
                    _postResponse.value = response.body()
                    Log.d("RegisterViewModel", "postRegister: ${response.body()}")
                } else {
                    // Handle unsuccessful response
                    val errorBody = response.errorBody()?.string()
                    val jsonObject = JSONObject(errorBody)
                    val message = jsonObject.getString("message")
                    Log.d("RegisterViewModel", "postRegister: Error - $message")
                    _postResponse.value = RegisterResponse(true, message)
                }
            } catch (e: HttpException) {
                // Handle HTTP error here
                _postResponse.value = RegisterResponse(true, e.message())
            } catch (e: Exception) {
                // Handle other exceptions here
            }
        }
    }

}