package com.gaia.chargebar

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
    }
}