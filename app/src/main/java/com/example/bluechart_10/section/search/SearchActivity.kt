package com.example.bluechart_10.section.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bluechart_10.R
import com.example.bluechart_10.core.bluetooth.BluetoothRepository
import com.example.bluechart_10.mvvm.ViewModelFactory
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    private lateinit var searchViewModel: SearchViewModel

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
    }

    private fun initViewModel() {
        val bluetoothRepository = BluetoothRepository()
        val viewModelFactory = ViewModelFactory{
            SearchViewModel(bluetoothRepository)
        }
        searchViewModel = ViewModelProvider(this@SearchActivity, viewModelFactory).get(SearchViewModel::class.java)
        searchViewModel.pairedDeviceList.observe(this@SearchActivity, Observer {
            (rcv_paired_devices.adapter as SearchAdapter).updateSearchList(it)
        })
    }

    private fun init() {
        searchViewModel.updatePairedDeviceList()
    }

    private fun initPairedRcv() {
        rcv_paired_devices.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = SearchAdapter(SearchAdapter.ListType.Paired)
        }
    }
}