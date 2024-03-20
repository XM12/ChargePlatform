package com.gaia.basic.utils

import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.util.Log
import android.util.Size
import java.util.Collections
import java.util.Collections.reverse
import java.util.Collections.sort

object CameraUtils {
    private const val TAG = "CameraUtils"

    fun chooseOptimalSize(
        cameraManager: CameraManager, cameraId: String, clazz: Class<*>, width: Int, height: Int, aspectRatio: Size?
    ): Size? {
        val characteristics = cameraManager.getCameraCharacteristics(cameraId)
        val configuration = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val choices = configuration?.getOutputSizes(clazz)
        var bigEnough: List<Size>? = null
        if (aspectRatio != null) {
            val w = aspectRatio.width
            val h = aspectRatio.height
            bigEnough = choices?.filter {
                it.height == it.width * h / w && it.width >= width && it.height >= height
            }
        }
        return if (!bigEnough.isNullOrEmpty()) {
            Collections.min(bigEnough, CompareSizesByArea())
        } else {
            choices?.let { choices[0] }
        }
    }

    fun chooseVideoSize(cameraManager: CameraManager, cameraId: String, clazz: Class<*>): Size? {
        val characteristics = cameraManager.getCameraCharacteristics(cameraId)
        val configuration = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val choices = configuration?.getOutputSizes(clazz)
        return if (choices != null) {
            val size = choices.firstOrNull {
                it.width == it.height * 4 / 3 && it.width <= 1080
            }
            size ?: choices[choices.size - 1]
        } else {
            null
        }
    }

    fun getOptimalSize(cameraManager: CameraManager, cameraId: String, width: Int, height: Int): Size? {
        val characteristics = cameraManager.getCameraCharacteristics(cameraId)
        val configuration = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        val sizeMap = configuration?.getOutputSizes(SurfaceTexture::class.java)
        val sizeList: MutableList<Size> = ArrayList()
        sizeMap?.also {
            for (option in it) {
                if (option == null) {
                    continue
                }
                if (width > height) {
                    if (option.width > width && option.height > height) {
                        sizeList.add(option)
                    }
                } else {
                    if (option.width > height && option.height > width) {
                        sizeList.add(option)
                    }
                }
            }
        }
        return if (sizeList.size > 0) {
            Collections.min(
                sizeList
            ) { o1, o2 -> java.lang.Long.signum((o1.width * o1.height - o2.width * o2.height).toLong()) }
        } else null
    }

    /**
     * 根据输出类获取指定相机的输出尺寸列表，降序排序
     */
    fun getCameraOutputSizes(cameraManager: CameraManager, cameraId: String): List<Size?>? {
        try {
            val characteristics = cameraManager.getCameraCharacteristics(cameraId)
            val configuration = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            val sizes = configuration?.getOutputSizes(SurfaceTexture::class.java)?.toList()
            sizes?.let {
                sort(
                    it
                ) { o1, o2 -> o1.width * o1.height - o2.width * o2.height }
            }
            sizes?.let { reverse(it) }
            return sizes
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        return null
    }

    fun getCameraId(cameraManager: CameraManager, lens: Int): String? {
        try {
            for (cameraId in cameraManager.cameraIdList) {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                //检查相机是否支持camera2
                val capabilities = characteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES)
                if (capabilities == null || capabilities.isEmpty()) {
                    Log.e(TAG, "openCamera: $cameraId no support camera2")
                    continue
                }
                val level = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
                if (level == null || level == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                    Log.e(TAG, "openCamera: $cameraId no support camera2")
                    continue
                }
                val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
                if (facing != null && facing == lens) {
                    //获取相机支持预览和拍摄配置，如预览尺寸、拍摄分辨率、帧率
                    val configuration = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    if (configuration != null) {
                        return cameraId
                    }
                }
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
        return null
    }

    fun switchCamera(cameraManager: CameraManager, camera: CameraDevice): String? {
        val characteristics = cameraManager.getCameraCharacteristics(camera.id)
        val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
        return if (facing == CameraCharacteristics.LENS_FACING_BACK) {
            getCameraId(cameraManager, CameraCharacteristics.LENS_FACING_FRONT)
        } else {
            getCameraId(cameraManager, CameraCharacteristics.LENS_FACING_BACK)
        }
    }

    class CompareSizesByArea : Comparator<Size> {
        override fun compare(lhs: Size, rhs: Size): Int {
            return java.lang.Long.signum(
                lhs.width.toLong() * lhs.height -
                        rhs.width.toLong() * rhs.height
            )
        }
    }

}