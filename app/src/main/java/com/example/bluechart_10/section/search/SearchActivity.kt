package com.example.bluechart_10.section.search

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bluechart_10.R
import com.example.bluechart_10.core.bluetooth.BluetoothRepository
import com.example.bluechart_10.mvvm.ViewModelFactory
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

class SearchActivity : AppCompatActivity() {
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchReceiver: SearchReceiver

    companion object {
        fun newInstance(context: Context): Intent {
            return Intent(context, SearchActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initView()
        initViewModel()
        init()
    }

    private fun initView() {
        initPairedRcv()
        initSearchedRcv()

        btn_search_devices.setOnClickListener {
            searchViewModel.searchDevices()
        }
    }

    private fun initViewModel() {
        val bluetoothRepository = BluetoothRepository()
        val viewModelFactory = ViewModelFactory{
            SearchViewModel(bluetoothRepository, application)
        }
        searchViewModel = ViewModelProvider(this@SearchActivity, viewModelFactory).get(SearchViewModel::class.java)
        searchViewModel.pairedDeviceList.observe(this@SearchActivity, Observer {
            if (it.isEmpty()) {
                rcv_paired_devices.visibility = GONE
                tv_paired_devices_no_data.visibility = VISIBLE
            } else {
                rcv_paired_devices.visibility = VISIBLE
                tv_paired_devices_no_data.visibility = GONE
                (rcv_paired_devices.adapter as SearchAdapter).updateSearchList(it)
            }
        })
        searchViewModel.searchedDeviceList.observe(this@SearchActivity, Observer {
            if (it.isEmpty()) {
                rcv_searched_devices.visibility = GONE
                tv_searched_devices_no_data.visibility = VISIBLE
            } else {
                rcv_searched_devices.visibility = VISIBLE
                tv_searched_devices_no_data.visibility = GONE
                (rcv_searched_devices.adapter as SearchAdapter).updateSearchList(it)
            }
        })

        searchViewModel.pairedState.observe(this@SearchActivity, Observer {
            tv_status_search.text = it
        })
    }

    private fun init() {
        searchViewModel.updatePairedDeviceList()
        searchViewModel.updateSearchedDeviceList(LinkedList())
        searchViewModel.updatePairedState()
        registerReceiver()
    }

    private fun registerReceiver() {
        val searchedDeviceList = LinkedList<BluetoothDevice>()

        searchReceiver = SearchReceiver(object : SearchCallback {
            override fun getDevice(bluetoothDevice: BluetoothDevice) {
                searchedDeviceList.add(bluetoothDevice)
                searchViewModel.updateSearchedDeviceList(searchedDeviceList)
            }

            override fun onSearchStart() {
                rcv_searched_devices.visibility = GONE
                tv_searched_devices_no_data.visibility = GONE
                searching_group.visibility = VISIBLE
            }

            override fun onSearchCompleted() {
                rcv_searched_devices.visibility = VISIBLE
                tv_searched_devices_no_data.visibility = GONE
                searching_group.visibility = GONE
            }
        })

        val intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(searchReceiver, intentFilter)
    }

    private fun initPairedRcv() {
        rcv_paired_devices.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = SearchAdapter(SearchAdapter.ListType.Paired)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun initSearchedRcv() {
        rcv_searched_devices.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = SearchAdapter(SearchAdapter.ListType.Searched)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }
}