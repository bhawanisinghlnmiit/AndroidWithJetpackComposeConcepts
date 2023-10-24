package com.example.androidwithjetpackcomposeconcepts.learning.foregroundservices

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ForegroundServiceViewModel: ViewModel() {

    private val _serviceIsRunning = MutableStateFlow(false)
    val serviceIsRunning: StateFlow<Boolean> = _serviceIsRunning

    fun stopService() {
        _serviceIsRunning.value = false
    }

    fun startService() {
        _serviceIsRunning.value = true
    }
}