package com.pinyou.basic.route

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter

object RouteManager {
    private val aRouter = ARouter.getInstance()

    fun startActivity(context: Context, path: String) {
        startActivity(context, path, null)
    }

    fun startActivity(context: Activity, path: String, requestCode: Int) {
        startActivity(context, path, null, requestCode)
    }

    fun startActivity(context: Activity, path: String, requestCode: Int, callback: NavCallback) {
        startActivity(context, path, null, requestCode, callback)
    }

    fun startActivity(context: Context, path: String, bundle: Bundle?) {
        aRouter.build(path).with(bundle).navigation(context)
    }

    fun startActivity(context: Activity, path: String, bundle: Bundle?, requestCode: Int) {
        aRouter.build(path).with(bundle).navigation(context, requestCode)
    }

    fun startActivity(context: Activity, path: String, bundle: Bundle?, requestCode: Int, callback: NavCallback) {
        aRouter.build(path).with(bundle).navigation(context, requestCode, callback)
    }

}