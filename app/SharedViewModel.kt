package com.example.monitorapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _tempLiveData = MutableLiveData<Double>()
    val tempLiveData: LiveData<Double> get() = _tempLiveData

    fun updateTemp(newTemp: Double) {
        _tempLiveData.value = newTemp
    }
}
