package com.example.yourstory.viewmodel.story.addstory;

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

public class AddStoryViewModel(
        val repository:Repository,
        val sessionManager:SessionManager
) : ViewModel() {
//        val _storiesList = MutableLiveData<List<StoryResponseData>>()
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

        fun uploadImagePost(imageMultipart : MultipartBody.Part, description: RequestBody) {
                viewModelScope.launch {
                        try {
                                getSessionToken()

                                val response = repository.POSTStory(
                                        _token,
                                        description,
                                        imageMultipart
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



//
//        fun checkLoginStatus(): Boolean {
//                return sessionManager.isLoggedIn()
//        }
//
//        fun clearAuthToken() {
//                sessionManager.clearAuthToken()
//        }
//
//        fun getToken() : String? {
//                return sessionManager.fetchAuthToken()
//        }
//
//        fun GETStoriesList(token: String, page: Int? = null, size: Int? = null, location: Int? = null) {
//                viewModelScope.launch {
//                        val response = repository.getAllStories(token, page, size, location)
//                        _storiesList.value = response.body()?.listStory
//                }
//        }
}
