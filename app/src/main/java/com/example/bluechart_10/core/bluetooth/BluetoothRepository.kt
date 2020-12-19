package com.example.bluechart_10.core.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

class BluetoothRepository {
    private val mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    fun getDeviceName(): String{
        return mBluetoothAdapter.name
    }

    fun getPairedList(): List<BluetoothDevice> {
        return mBluetoothAdapter.bondedDevices.toList()
    }
}