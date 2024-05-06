package com.pinyou.basic.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.util.Locale

object DeviceUtils {
    /**
     * 获取设备宽度（px）
     */
    fun getDeviceWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

    /**
     * 获取设备高度（px）
     */
    fun getDeviceHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    /**
     * 获取厂商名
     */
    fun getDeviceManufacturer(): String {
        return android.os.Build.MANUFACTURER
    }

    /**
     * 获取产品名
     */
    fun getDeviceProduct(): String {
        return android.os.Build.PRODUCT
    }

    /**
     * 获取手机品牌
     */
    fun getDeviceBrand(): String {
        return android.os.Build.BRAND
    }

    /**
     * 获取手机型号
     */
    fun getDeviceModel(): String {
        return android.os.Build.MODEL
    }

    /**
     * 获取手机主板名
     */
    fun getDeviceBoard(): String {
        return android.os.Build.BOARD
    }

    /**
     * 设备名
     */
    fun getDeviceDevice(): String {
        return android.os.Build.DEVICE
    }

    /**
     *
     *
     * fingerprint 信息
     */
    fun getDeviceFingerprint(): String {
        return android.os.Build.FINGERPRINT
    }

    /**
     * 硬件名
     */
    fun getDeviceHardware(): String {
        return android.os.Build.HARDWARE
    }

    /**
     * 主机
     */
    fun getDeviceHost(): String {
        return android.os.Build.HOST
    }

    /**
     * 显示ID
     */
    fun getDeviceDisplay(): String {
        return android.os.Build.DISPLAY
    }

    /**
     * ID
     */
    fun getDeviceId(): String {
        return android.os.Build.ID
    }

    /**
     * 获取手机用户名
     */

    fun getDeviceUser(): String {
        return android.os.Build.USER
    }

    /**
     * 获取手机 硬件序列号
     */
    fun getDeviceSerial(): String {
        return android.os.Build.SERIAL
    }

    /**
     * 获取手机Android 系统SDK
     */
    fun getDeviceSDK(): Int {
        return android.os.Build.VERSION.SDK_INT
    }

    /**
     * 获取手机Android 版本
     */
    fun getDeviceAndroidVersion(): String {
        return android.os.Build.VERSION.RELEASE
    }

    /**
     * 获取手机系统语言
     */
    fun getDeviceDefaultLanguage(): String {
        return Locale.getDefault().language
    }

    /**
     * 获取手机cpu架构
     */
    fun getCpuAbi(): String {
        return try {
            val cpuAbi = BufferedReader(
                InputStreamReader(Runtime.getRuntime().exec("getprop ro.product.cpu.abi").inputStream)
            ).readLine()
            cpuAbi
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

}