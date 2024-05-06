package com.pinyou.basic.log

import com.tencent.mars.xlog.Log

object LogUtils {

    fun v(tag: String, message: String) {
        Log.v(tag, message)
    }

    fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    fun e(tag: String, message: String) {
        Log.e(tag, message)
    }
}