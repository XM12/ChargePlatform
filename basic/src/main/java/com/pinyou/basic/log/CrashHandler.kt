package com.pinyou.basic.log

import android.app.Application
import com.pinyou.basic.utils.AppUtils
import com.pinyou.basic.utils.DateUtils
import com.pinyou.basic.utils.DeviceUtils
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter

object CrashHandler : Thread.UncaughtExceptionHandler {
    private const val TAG = "CHARGE_CRASH"
    private var defaultCrashHandler: Thread.UncaughtExceptionHandler? = null
    private var context: Application? = null
    private val deviceInfo = mutableMapOf<String, String>()

    fun init(context: Application) {
        CrashHandler.context = context
        defaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        collectDeviceInfo()
        //保存异常信息到本地
        saveCrashToLocal(exception)
        //系统默认处理
        defaultCrashHandler?.uncaughtException(thread, exception)
    }

    private fun saveCrashToLocal(exception: Throwable) {
        val buffer = StringBuffer()
        buffer.append("EXCEPTION\n")
        buffer.append("time=${DateUtils.format(System.currentTimeMillis(), DateUtils.Y_M_D_H_M_S_S)}\n")
        deviceInfo.forEach {
            buffer.append(it.key + "=" + it.value + "\n")
        }
        var fileOutputStream: FileOutputStream? = null
        var bufferedOutputStream: BufferedOutputStream? = null
        try {
            val fileName = "crash_${DateUtils.format(System.currentTimeMillis(), DateUtils.Y_M_D_1)}.log"
            val writer = StringWriter()
            //向文本输出流打印对象的格式化表示形式
            val printWriter = PrintWriter(writer)
            //将此 throwable 及其追踪输出至标准错误流
            exception.printStackTrace(printWriter)
            var cause = exception.cause
            while (cause != null) {
                //异常链
                cause.printStackTrace()
                cause = cause.cause
            }
            printWriter.close()
            buffer.append(writer.toString())
            LogUtils.e(TAG, buffer.toString())
            val path = (context?.filesDir?.path ?: "") + (File.separator + "crash")
            val crashDir = File(path)
            if (!crashDir.exists() && !crashDir.mkdirs()) {
                LogUtils.v(TAG, "crash dir create failure, $path")
                return
            }
            val file = File(path, fileName)
            if (!file.exists() && !file.createNewFile()) {
                LogUtils.v(TAG, "crash file create failure, ${file.path}")
                return
            }
            fileOutputStream = FileOutputStream(file, true)
            bufferedOutputStream = BufferedOutputStream(fileOutputStream)
            bufferedOutputStream.write(buffer.toString().toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.e(TAG, "save cache to local error, ${e.stackTrace}")
        } finally {
            bufferedOutputStream?.flush()
            bufferedOutputStream?.close()
            fileOutputStream?.flush()
            fileOutputStream?.close()
        }
    }

    private fun collectDeviceInfo() {
        deviceInfo["versionName"] = AppUtils.getPackageName(context?.applicationContext)
        deviceInfo["versionCode"] = AppUtils.getVersionCode(context?.applicationContext).toString()
        deviceInfo["手机厂商"] = DeviceUtils.getDeviceManufacturer()
        deviceInfo["手机型号"] = DeviceUtils.getDeviceModel()
        deviceInfo["手机系统版本号"] = DeviceUtils.getDeviceSDK().toString()
        deviceInfo["手机CPU架构"] = DeviceUtils.getCpuAbi()
    }
}
