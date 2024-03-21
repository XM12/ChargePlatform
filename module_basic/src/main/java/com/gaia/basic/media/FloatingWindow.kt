package com.gaia.basic.media

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.hardware.camera2.CameraDevice
import android.os.Build
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout

@SuppressLint("ClickableViewAccessibility")
class FloatingWindow(private val context: Context) : View.OnTouchListener {
    companion object {
        private const val TAG = "FloatingWindow"
    }

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var videoManager: VideoManager? = null
    private var previewManager = PreviewManager(context)
    private val floatingView = FrameLayout(context)
    private var layoutParams: WindowManager.LayoutParams? = null
    private var x = 0
    private var y = 0

    fun initWindow() {
        val outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        layoutParams = WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
            format = PixelFormat.RGBA_8888
            flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            //位置大小设置
            width = 400
            height = 400
            gravity = Gravity.START or Gravity.TOP
            //设置剧中屏幕显示
            x = outMetrics.widthPixels / 2 - width / 2
            y = outMetrics.heightPixels / 2 - height / 2
        }
        windowManager.addView(floatingView, layoutParams)
        floatingView.setOnTouchListener(this)
        initCamera()
    }

    private fun initCamera() {
        previewManager.initPreview(floatingView)
        previewManager.initCamera {
            if (it) {
                videoManager = videoManager ?: VideoManager(context, previewManager)
                videoManager!!.startLoopRecording()
            }
        }
    }

    fun removeWindow() {
        previewManager.release()
        windowManager.removeView(floatingView)
        videoManager?.releaseRecorder()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                x = event.rawX.toInt()
                y = event.rawY.toInt()
            }

            MotionEvent.ACTION_MOVE -> {
                val nowX = event.rawX.toInt()
                val nowY = event.rawY.toInt()
                val movedX = nowX - x
                val movedY = nowY - y
                x = nowX
                y = nowY
                layoutParams?.apply {
                    x += movedX
                    y += movedY
                }
                windowManager.updateViewLayout(floatingView, layoutParams)
            }

            else -> {

            }
        }
        return true
    }
}