package com.example.monitorapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {
    private val _tempLiveData = MutableLiveData<Double>()
    val tempLiveData: LiveData<Double> get() = _tempLiveData

    fun updateTemp(newTemp: Double) {
        _tempLiveData.value = newTemp
        Log.d("MyApp", "전달: ${_tempLiveData.value}")
    }

//    private val _tempLiveData = MutableLiveData<Double>()
//    private val _humidLiveData = MutableLiveData<Double>()
//    private val _bodyLiveData = MutableLiveData<Double>()
//    private val _uvlLiveData = MutableLiveData<Int>()
//    private val _fireLiveData = MutableLiveData<Int>()
//
//    // LiveData를 불변으로 노출
//    val tempLiveData: LiveData<Double>
//        get() = _tempLiveData
//
//    val humidLiveData: LiveData<Double>
//        get() = _humidLiveData
//
//    val bodyLiveData: LiveData<Double>
//        get() = _bodyLiveData
//
//    val uvlLiveData: LiveData<Int>
//        get() = _uvlLiveData
//
//    val fireLiveData: LiveData<Int>
//        get() = _fireLiveData
//
//    val combinedLiveData: LiveData<Pair<Triple<Double, Double, Double>, Pair<Int, Int>>> = MediatorLiveData<Pair<Triple<Double, Double, Double>, Pair<Int, Int>>>().apply {
//        addSource(_tempLiveData) { temp ->
//            // _tempLiveData 값이 변경될 때마다 실행
//            val humid = _humidLiveData.value ?: 0.0 // _humidLiveData의 현재 값 또는 기본값
//            val body = _bodyLiveData.value ?: 0.0 // _bodyLiveData의 현재 값 또는 기본값
//            val uvl = _uvlLiveData.value ?: 0 // _uvlLiveData의 현재 값 또는 기본값
//            val fire = _fireLiveData.value ?: 0 // _fireLiveData의 현재 값 또는 기본값
//            value = Triple(temp, humid, body) to Pair(uvl, fire) // temp, humid, body, uvl, fire를 결합하여 값을 설정
//        }
//        addSource(_humidLiveData) { humid ->
//            // _humidLiveData 값이 변경될 때마다 실행
//            val temp = _tempLiveData.value ?: 0.0 // _tempLiveData의 현재 값 또는 기본값
//            val body = _bodyLiveData.value ?: 0.0 // _bodyLiveData의 현재 값 또는 기본값
//            val uvl = _uvlLiveData.value ?: 0 // _uvlLiveData의 현재 값 또는 기본값
//            val fire = _fireLiveData.value ?: 0 // _fireLiveData의 현재 값 또는 기본값
//            value = Triple(temp, humid, body) to Pair(uvl, fire) // temp, humid, body, uvl, fire를 결합하여 값을 설정
//        }
//        addSource(_bodyLiveData) { body ->
//            // _bodyLiveData 값이 변경될 때마다 실행
//            val temp = _tempLiveData.value ?: 0.0 // _tempLiveData의 현재 값 또는 기본값
//            val humid = _humidLiveData.value ?: 0.0 // _humidLiveData의 현재 값 또는 기본값
//            val uvl = _uvlLiveData.value ?: 0 // _uvlLiveData의 현재 값 또는 기본값
//            val fire = _fireLiveData.value ?: 0 // _fireLiveData의 현재 값 또는 기본값
//            value = Triple(temp, humid, body) to Pair(uvl, fire) // temp, humid, body, uvl, fire를 결합하여 값을 설정
//        }
//        addSource(_uvlLiveData) { uvl ->
//            // _uvlLiveData 값이 변경될 때마다 실행
//            val temp = _tempLiveData.value ?: 0.0 // _tempLiveData의 현재 값 또는 기본값
//            val humid = _humidLiveData.value ?: 0.0 // _humidLiveData의 현재 값 또는 기본값
//            val body = _bodyLiveData.value ?: 0.0 // _bodyLiveData의 현재 값 또는 기본값
//            val fire = _fireLiveData.value ?: 0 // _fireLiveData의 현재 값 또는 기본값
//            value = Triple(temp, humid, body) to Pair(uvl, fire) // temp, humid, body, uvl, fire를 결합하여 값을 설정
//        }
//        addSource(_fireLiveData) { fire ->
//            // _fireLiveData 값이 변경될 때마다 실행
//            val temp = _tempLiveData.value ?: 0.0 // _tempLiveData의 현재 값 또는 기본값
//            val humid = _humidLiveData.value ?: 0.0 // _humidLiveData의 현재 값 또는 기본값
//            val body = _bodyLiveData.value ?: 0.0 // _bodyLiveData의 현재 값 또는 기본값
//            val uvl = _uvlLiveData.value ?: 0 // _uvlLiveData의 현재 값 또는 기본값
//            value = Triple(temp, humid, body) to Pair(uvl, fire) // temp, humid, body, uvl, fire를 결합하여 값을 설정
//        }
//    }
//
//    // temp 값을 업데이트하는 메서드
//    fun updateTemp(newTemp: Double) {
//        _tempLiveData.value = newTemp
//    }
//
//    fun updateHumid(newHumid: Double) {
//        _humidLiveData.value = newHumid
//    }
//
//    fun updateBody(newBody: Double) {
//        _bodyLiveData.value = newBody
//    }
//
//    fun updateUvl(newUvl: Int) {
//        _uvlLiveData.value = newUvl
//    }
//
//    fun updateFire(newFire: Int) {
//        _fireLiveData.value = newFire
//    }
}