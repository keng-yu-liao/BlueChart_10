package com.example.bluechart_10.core.bluetooth

import android.bluetooth.BluetoothAdapter

class BluetoothRepository {
    fun getDeviceName(): String{
        return BluetoothAdapter.getDefaultAdapter().name
    }
}