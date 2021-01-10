package com.example.bluechart_10.core.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import com.example.bluechart_10.common.BluetoothConfig
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothThreadManager(private val bluetoothAdapter: BluetoothAdapter,
                             private val connectName: String,
                             private val uuid: UUID,
                             private val handler: android.os.Handler
) {
    private lateinit var acceptThread: AcceptThread
    private lateinit var connectThread: ConnectThread
    private lateinit var connectedThread: ConnectedThread

    fun listen() {
        acceptThread = AcceptThread()
        acceptThread.run()
    }

    fun connect(device: BluetoothDevice) {
        connectThread = ConnectThread(device)
        connectThread.run()
    }

    fun stopBluetoothChat() {
        if (this::acceptThread.isInitialized) acceptThread.cancel()
        if (this::connectThread.isInitialized) connectThread.cancel()
        if (this::connectedThread.isInitialized) connectedThread.cancel()
    }

    fun writeMsg(msg: String) {
        connectedThread.write(msg)
    }

    private fun manageConnectedSocket(socket: BluetoothSocket) {
        connectedThread = ConnectedThread(socket)
        connectedThread.run()
    }

    //接收執行緒
    private inner class AcceptThread : Thread() {

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            bluetoothAdapter.listenUsingRfcommWithServiceRecord(connectName, uuid)
        }

        override fun run() {
            var shouldLoop = true
            while (shouldLoop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()
                } catch (e: IOException) {
                    handler.obtainMessage(BluetoothConfig.BLUETOOTH_ERROR, e.message).sendToTarget()
                    shouldLoop = false
                    null
                }

                socket?.let { blueSocket ->
                    manageConnectedSocket(blueSocket)
                    mmServerSocket?.close()
                    shouldLoop = false
                }
            }
        }

        fun cancel() {
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
                handler.obtainMessage(BluetoothConfig.BLUETOOTH_ERROR, e.message).sendToTarget()
            }
        }
    }

    //連接執行緒
    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            device.createRfcommSocketToServiceRecord(uuid)
        }

        override fun run() {
            bluetoothAdapter.cancelDiscovery()

            mmSocket?.use { socket ->
                socket.connect()
                manageConnectedSocket(socket)
            }
        }

        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                handler.obtainMessage(BluetoothConfig.BLUETOOTH_ERROR, e.message).sendToTarget()
            }
        }
    }

    //連接後執行緒
    private inner class ConnectedThread(private val socket: BluetoothSocket) : Thread() {

        private val mmInStream: InputStream = socket.inputStream
        private val mmOutStream: OutputStream = socket.outputStream
        private val mmByteArray: ByteArray = ByteArray(1024)

        override fun run() {
            var numBytes: Int

            while (true) {
                numBytes = try {
                    mmInStream.read(mmByteArray)
                } catch (e: IOException) {
                    handler.obtainMessage(BluetoothConfig.BLUETOOTH_ERROR, e.message)
                    break
                }

                val receivedMsg = String(mmByteArray.copyOf(numBytes))
                handler.obtainMessage(BluetoothConfig.BLUETOOTH_RECEIVE, receivedMsg)
            }
        }

        fun write(msg: String) {
            try {
                mmOutStream.write(msg.toByteArray())
            } catch (e: IOException) {
                handler.obtainMessage(BluetoothConfig.BLUETOOTH_ERROR, e.message)
            }
        }

        fun cancel() {
            try {
                socket.close()
            } catch (e: IOException) {
                handler.obtainMessage(BluetoothConfig.BLUETOOTH_ERROR, e.message)
            }
        }
    }
}