package com.example.bluechart_10.section.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bluechart_10.core.bluetooth.BluetoothRepository

class LoginViewModel(repository: BluetoothRepository) : ViewModel() {
    val deviceName = MutableLiveData<String>().apply {
        value = repository.getDeviceName()
    }
}