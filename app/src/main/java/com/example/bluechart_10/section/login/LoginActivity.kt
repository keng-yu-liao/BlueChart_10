package com.example.bluechart_10.section.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bluechart_10.R
import com.example.bluechart_10.core.bluetooth.BluetoothRepository
import com.example.bluechart_10.mvvm.ViewModelFactory
import com.example.bluechart_10.section.search.SearchActivity
import com.example.liaoutils.dialog.LiaoDialog

import kotlinx.android.synthetic.main.activity_login.*
import kotlin.system.exitProcess

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
        initViewModel()
    }

    override fun onResume() {
        super.onResume()
        checkBluetoothEnable()
    }

    private fun initView() {
        btn_login.setOnClickListener {
            startActivity(SearchActivity.newInstance(this@LoginActivity))
        }
    }

    private fun initViewModel() {
        val bluetoothRepository = BluetoothRepository()
        val viewModelFactory = ViewModelFactory{
            LoginViewModel(bluetoothRepository)
        }
        loginViewModel = ViewModelProvider(this@LoginActivity, viewModelFactory).get(LoginViewModel::class.java)
        loginViewModel.deviceName.observe(this@LoginActivity, Observer {
            tv_device_name.text = it
        })
    }

    private fun checkBluetoothEnable() {
        if (!loginViewModel.isBluetoothEnable()) {
            LiaoDialog.getNormalDialog(
                    this@LoginActivity,
                    getString(R.string.dialog_bluetooth_hint),
                    getString(R.string.dialog_btn_yes),
                    {_,_-> loginViewModel.enableBluetooth()},
                    getString(R.string.dialog_btn_cancel),
                    {_,_-> closeApp()}
            ).show()
        }
    }

    private fun closeApp() {
        exitProcess(0)
    }
}