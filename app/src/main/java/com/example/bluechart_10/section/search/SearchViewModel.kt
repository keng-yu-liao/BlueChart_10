package com.example.bluechart_10.section.search

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bluechart_10.core.bluetooth.BluetoothRepository

class SearchViewModel(private val bluetoothRepository: BluetoothRepository) : ViewModel() {
    val pairedDeviceList = MutableLiveData<List<BluetoothDevice>>().apply {
        value = bluetoothRepository.getPairedList().toList()
    }

    fun updatePairedDeviceList() {
        pairedDeviceList.value = bluetoothRepository.getPairedList().toList()
    }
}