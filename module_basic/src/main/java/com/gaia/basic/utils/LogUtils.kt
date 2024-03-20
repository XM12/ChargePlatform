package com.gaia.basic.utils

import android.util.Log

object LogUtils {

    fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    fun e(tag: String, message: String) {
        Log.e(tag, message)
    }
}