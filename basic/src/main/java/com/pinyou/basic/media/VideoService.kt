package com.pinyou.basic.media

import android.app.Service
import android.content.Intent
import android.os.IBinder

class VideoService : Service() {
    private lateinit var floatingWindow: FloatingWindow

    override fun onCreate() {
        super.onCreate()
        floatingWindow = FloatingWindow(this)
        floatingWindow.initWindow()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        floatingWindow.removeWindow()
    }

}