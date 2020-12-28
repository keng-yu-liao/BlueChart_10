package com.example.bluechart_10.section.search

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class SearchReceiver(private val searchCallback: SearchCallback) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        intent.action?.let { action ->
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)?.let { bluetoothDevice ->
                        searchCallback.getDevice(bluetoothDevice)
                    }
                }

                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    searchCallback.onSearchStart()
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    searchCallback.onSearchCompleted()
                }

                else -> {}
            }
        }
    }
}

interface SearchCallback {
    fun getDevice(bluetoothDevice: BluetoothDevice)
    fun onSearchStart()
    fun onSearchCompleted()
}