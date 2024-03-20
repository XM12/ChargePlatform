package com.gaia.chargebar

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.gaia.db.AppDbManager
import com.gaia.public_pile.PublicPileManager

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
        PublicPileManager.authorizePrivacyAgreement(this)
        AppDbManager.instance.init(this)
    }
}