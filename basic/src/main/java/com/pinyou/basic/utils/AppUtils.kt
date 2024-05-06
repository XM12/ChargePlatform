package com.pinyou.basic.utils

import android.content.Context
import android.os.Build

object AppUtils {

    fun getPackageName(context: Context?): String {
        return context?.packageName ?: ""
    }

    fun getVersionName(context: Context?): String {
        return context?.let {
            val packageManager = it.packageManager
            val packageInfo = packageManager.getPackageInfo(getPackageName(it), 0)
            packageInfo.versionName
        } ?: "1.0.0"
    }

    fun getVersionCode(context: Context?): Long {
        return context?.let {
            val packageManager = it.packageManager
            val packageInfo = packageManager.getPackageInfo(getPackageName(it), 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                packageInfo.versionCode.toLong()
            }
        } ?: 1

    }
}