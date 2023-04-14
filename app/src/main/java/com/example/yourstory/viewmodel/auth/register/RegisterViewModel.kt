package com.example.yourstory.viewmodel.auth.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourstory.model.RegisterResponse
import com.example.yourstory.model.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    private var _number: MutableLiveData<Int> = MutableLiveData(0)
    var _postResponse: MutableLiveData<RegisterResponse> = MutableLiveData()

    val number: MutableLiveData<Int>
        get() = _number

    fun increment() {
        _number.value = _number.value?.plus(1)
    }

    fun postRegister(name : String, email : String, password : String) {
        viewModelScope.launch {
            try {
                val response = repository.postRegister(name, email, password)
                _postResponse.value = response
            } catch (e: HttpException) {
                // Handle HTTP error here
                when (e.code()) {
                    400 -> {
                        // Handle HTTP 400 error
                        Log.d("RegisterActivity", "postRegister: ${e.message()}")
                        _postResponse.value = RegisterResponse(false, e.message())

                    }
                    // Handle other HTTP errors here
                    else -> {
                        // Handle other HTTP errors
                    }
                }
            } catch (e: Exception) {
                // Handle other exceptions here
            }
        }
    }

}