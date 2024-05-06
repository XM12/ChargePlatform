package com.pinyou.basic.log

import android.content.Context
import com.pinyou.basic.BuildConfig
import com.tencent.mars.xlog.Log
import com.tencent.mars.xlog.Xlog
import java.io.File

object XlogManager {

    fun init(context: Context) {
        System.loadLibrary("c++_shared")
        System.loadLibrary("marsxlog")

        val filePath = context.filesDir?.absolutePath
        val logDir = File(filePath + File.separator + "xlog_file")
        if (!logDir.exists()) {
            logDir.mkdirs()
        }
        val cacheDir = File(filePath + File.separator + "xlog_cache")
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }

        val xlog = Xlog()
        xlog.appenderOpen(
            if (BuildConfig.DEBUG) Xlog.LEVEL_ALL else Xlog.LEVEL_INFO,
            Xlog.AppednerModeAsync,
            cacheDir.path,
            logDir.path,
            "chargebar",
            0
        )
        Log.setLogImp(xlog)
        Log.setConsoleLogOpen(BuildConfig.DEBUG)
    }

    /**
     * 将.mmap3文件缓存的日志写入.xlog文件中
     * */
    fun flush() {
        Log.appenderFlush()
    }

    fun destroy() {
        Log.appenderClose()
    }

}