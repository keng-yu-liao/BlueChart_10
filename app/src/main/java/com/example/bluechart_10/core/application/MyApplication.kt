package com.example.bluechart_10.core.application

import android.app.Application
import android.content.Context
import com.example.bluechart_10.R
import com.example.liaoutils.dialog.LiaoDialog

class MyApplication : Application() {
    companion object {
        lateinit var mContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        LiaoDialog.setDialogTitle(mContext.getString(R.string.dialog_title))
    }
}