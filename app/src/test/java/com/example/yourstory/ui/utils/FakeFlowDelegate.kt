package com.example.yourstory.ui.utils

import kotlinx.coroutines.flow.MutableSharedFlow

class FakeFlowDelegate<T> {
    val flow: MutableSharedFlow<T> = MutableSharedFlow()

    suspend fun emit(value: T) = flow.emit(value)
}