package com.example.monitorapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.monitorapp.databinding.ActivityValuetestBinding

class ValuetestActivity : AppCompatActivity() {

    private lateinit var valueViewModel: DataViewModel

    private var mBinding: ActivityValuetestBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityValuetestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        valueViewModel = ViewModelProvider(this).get(DataViewModel::class.java)

        valueViewModel.tempLiveData.observe(this, { newTemp ->
            Log.d("MyApp", "observe 블록 실행됨")
            binding.tempValue.text = "$newTemp°C" // temp 값을 TextView에 설정
        })
    }
}