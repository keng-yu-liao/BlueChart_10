package com.example.bluechart_10.section.search

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bluechart_10.R
import kotlinx.android.synthetic.main.item_search_paired_device.view.*
import kotlin.collections.ArrayList

class SearchAdapter(private val listType: ListType) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var updateList: ArrayList<BluetoothDevice> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (listType == ListType.Paired) {
            PairedDeviceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_paired_device, parent, false))
        } else {
            SearchedDeviceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_search_paired_device, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return updateList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (listType == ListType.Paired) {
            (holder as PairedDeviceViewHolder).bind(updateList[position])
        } else {
            (holder as SearchedDeviceViewHolder).bind(updateList[position])
        }
    }

    fun updateSearchList(newUpdateList: List<BluetoothDevice>) {
        updateList.clear()
        updateList.addAll(newUpdateList)
        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return updateList[oldItemPosition].name == newUpdateList[newItemPosition].name
            }

            override fun getOldListSize(): Int {
                return updateList.size
            }

            override fun getNewListSize(): Int {
                return newUpdateList.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return updateList[oldItemPosition] == newUpdateList[newItemPosition]
            }
        }).dispatchUpdatesTo(this)
    }

    inner class PairedDeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(bluetoothDevice: BluetoothDevice) {
            itemView.tv_device_name.text = bluetoothDevice.name
        }
    }

    inner class SearchedDeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(bluetoothDevice: BluetoothDevice) {
            itemView.tv_device_name.text = bluetoothDevice.name
        }
    }

    enum class ListType {
        Paired,
        Searched
    }
}