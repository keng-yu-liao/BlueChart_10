package com.example.bluechart_10.core.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHeadset

class BluetoothRepository {
    private val mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    fun getDeviceName(): String{
        return mBluetoothAdapter.name
    }

    fun getPairedList(): List<BluetoothDevice> {
        return mBluetoothAdapter.bondedDevices.toList()
    }

    fun getConnectionState(): Int {
        return mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET)
    }

    fun startDiscovery() {
        mBluetoothAdapter.startDiscovery()
    }
}