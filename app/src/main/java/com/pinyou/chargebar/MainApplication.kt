package com.pinyou.chargebar

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.pinyou.basic.log.CrashHandler
import com.pinyou.db.AppDbManager
import com.pinyou.public_pile.PublicPileManager
import com.pinyou.basic.log.XlogManager

class MainApplication : Application() {
    companion object {
        const val TAG = "MainApplication"
    }

    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
        PublicPileManager.authorizePrivacyAgreement(this)
        AppDbManager.instance.init(this)
        XlogManager.init(this)
        CrashHandler.init(this)
        XlogManager.flush()
    }
}