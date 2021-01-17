package com.example.bluechart_10.section.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bluechart_10.core.bluetooth.BluetoothRepository

class LoginViewModel(private val mBluetoothRepository: BluetoothRepository) : ViewModel() {
    val deviceName = MutableLiveData<String>().apply {
        value = mBluetoothRepository.getDeviceName()
    }

    fun enableBluetooth() {
        mBluetoothRepository.enableBluetooth()
    }

    fun isBluetoothEnable(): Boolean {
        return mBluetoothRepository.getBluetoothEnableStatus()
    }
}