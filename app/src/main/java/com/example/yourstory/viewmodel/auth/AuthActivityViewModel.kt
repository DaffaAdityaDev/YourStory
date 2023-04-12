package com.example.yourstory.viewmodel.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthActivityViewModel : ViewModel() {
    private var _number: MutableLiveData<Int> = MutableLiveData(0)
    val number: MutableLiveData<Int>
        get() = _number

    fun increment() {
        _number.value = _number.value?.plus(1)
    }
}