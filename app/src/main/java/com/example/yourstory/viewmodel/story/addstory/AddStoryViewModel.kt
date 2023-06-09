package com.example.yourstory.viewmodel.story.addstory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourstory.model.GetStoryResponse
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File

class AddStoryViewModel(
        val repository:Repository,
        val sessionManager:SessionManager
) : ViewModel() {
        var _fileImage: File? = null
        var _token = ""
        var _message = MutableLiveData<GetStoryResponse>()

        fun getFileImage(): File? {
                return _fileImage
        }

        fun setFileImage(file: File) {
                _fileImage =  file
        }

        fun getSessionToken() {
                _token = "Bearer " + sessionManager.fetchAuthToken() as String
        }

        fun uploadImagePost(
                imageMultipart : MultipartBody.Part,
                description: RequestBody,
                lat: Float? = null,
                lon: Float? = null) {
                viewModelScope.launch {
                        try {
                                getSessionToken()

                                val response = repository.POSTStory(
                                        _token,
                                        description,
                                        imageMultipart,
                                        lat,
                                        lon
                                )
                                if (response.isSuccessful) {
                                        val responseBody = response.body()
                                        if (responseBody != null && !responseBody.error) {
                                                Log.d("addStory", "Login successful: ${response.body()}")
                                                _message.value = response.body()
                                        } else {
                                                Log.d("addStory", "Login failed: ${response.body()}")
                                        }
                                } else {
                                        val errorBody = response.errorBody()?.string()
                                        val jsonObject = JSONObject(errorBody)
                                        val message = jsonObject.getString("message")
                                        Log.d("addStory", "Login failed: $message")
                                }

                        } catch(e: Exception) {
                                Log.d("addStory", "Login failed: $e")

                        }
                }
        }
}
