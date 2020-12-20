package com.example.bluechart_10.section.search

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.bluechart_10.R
import com.example.bluechart_10.core.bluetooth.BluetoothRepository

class SearchViewModel(private val bluetoothRepository: BluetoothRepository,
                      application: Application
) : AndroidViewModel(application) {
    val pairedDeviceList = MutableLiveData<List<BluetoothDevice>>()
    val pairedState = MutableLiveData<String>()

    fun updatePairedDeviceList() {
        pairedDeviceList.value = bluetoothRepository.getPairedList().toList()
    }

    fun updatePairedState() {
        pairedState.value = when(bluetoothRepository.getConnectionState()) {
            BluetoothProfile.STATE_DISCONNECTED -> getApplication<Application>().getString(R.string.bluetooth_status_disconnected)
            BluetoothProfile.STATE_CONNECTED -> getApplication<Application>().getString(R.string.bluetooth_status_connected)
            else -> getApplication<Application>().getString(R.string.bluetooth_status_disconnected)
        }
    }
}