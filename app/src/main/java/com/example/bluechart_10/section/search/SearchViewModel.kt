package com.example.bluechart_10.section.search

import android.app.Application
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.bluechart_10.R
import com.example.bluechart_10.core.bluetooth.BluetoothRepository
import java.util.*

class SearchViewModel(private val mBluetoothRepository: BluetoothRepository,
                      application: Application
) : AndroidViewModel(application) {
    val pairedDeviceList = MutableLiveData<List<BluetoothDevice>>()
    val searchedDeviceList = MutableLiveData<LinkedList<BluetoothDevice>>()
    val pairedState = MutableLiveData<String>()

    fun updatePairedDeviceList() {
        pairedDeviceList.value = mBluetoothRepository.getPairedList().toList()
    }

    fun updateSearchedDeviceList(searchedDeviceList: LinkedList<BluetoothDevice>) {
        this.searchedDeviceList.value = searchedDeviceList
    }

    fun updatePairedState() {
        pairedState.value = when(mBluetoothRepository.getConnectionState()) {
            BluetoothProfile.STATE_DISCONNECTED -> getApplication<Application>().getString(R.string.bluetooth_status_disconnected)
            BluetoothProfile.STATE_CONNECTED -> getApplication<Application>().getString(R.string.bluetooth_status_connected)
            else -> getApplication<Application>().getString(R.string.bluetooth_status_disconnected)
        }
    }

    fun searchDevices() {
        mBluetoothRepository.startDiscovery()
    }
}