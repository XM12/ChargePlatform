package com.gaia.basic.media

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.MediaRecorder
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import com.gaia.basic.utils.CameraUtils
import com.gaia.basic.view.AutoFitTextureView
import java.lang.ref.WeakReference

@Suppress("DEPRECATION")
class PreviewManager(private val context: Context) {

    companion object {
        private const val TAG = "PreviewManager"
        private const val MSG_START_PREVIEW = 0
        private const val MSG_START_RECORDING = 1
        private const val MSG_CAMERA_START_STATUS = 2
    }

    private var container: FrameLayout? = null
    private lateinit var preview: AutoFitTextureView
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var cameraHandler: Handler
    private var mainHandler: Handler
    private var previewSize: Size? = null
    private var session: CameraCaptureSession? = null
    private var captureRequestBuilder: CaptureRequest.Builder? = null
    private var camera: CameraDevice? = null
    private var previewSurface: Surface? = null
    private var previewSurfaceTexture: SurfaceTexture? = null
    private var videoSurface: Surface? = null
    private lateinit var cameraStartCallback: ((isSuccess: Boolean) -> Unit)
    private var width = 0
    private var height = 0

    init {
        val handlerThread = HandlerThread("${context.packageName}.camera")
        handlerThread.start()
        cameraHandler = Handler(handlerThread.looper)
        mainHandler = MainHandler(this)
    }

    private class MainHandler(previewManager: PreviewManager) : Handler(Looper.getMainLooper()) {
        private var weakReference = WeakReference(previewManager)

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val previewManager = weakReference.get()
            when (msg.what) {
                MSG_START_PREVIEW -> {
                    previewManager?.startPreview()
                }

                MSG_START_RECORDING -> {
                    val surface = msg.obj as Surface
                    previewManager?.startRecording(surface)
                }

                MSG_CAMERA_START_STATUS -> {
                    val isSuccess = msg.obj as Boolean
                    if (previewManager?.cameraStartCallback != null) {
                        previewManager.cameraStartCallback(isSuccess)
                    }
                }
            }
        }
    }

    fun getCameraId(): String? {
        return camera?.id
    }

    private fun sendStartStatus(isSuccess: Boolean) {
        val message = mainHandler.obtainMessage(MSG_CAMERA_START_STATUS)
        message.obj = isSuccess
        message.sendToTarget()
    }

    fun initPreview(container: FrameLayout) {
        this.container = container
        release()
        preview = AutoFitTextureView(context)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        container.addView(preview, params)
    }

    fun initCamera(cameraStartCallback: ((isSuccess: Boolean) -> Unit)) {
        this.cameraStartCallback = cameraStartCallback
        preview.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                this@PreviewManager.width = width
                this@PreviewManager.height = height
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
                configureTransform(width, height)
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                sendStartStatus(false)
                release()
                return true
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

            }
        }
    }

    /**
     * 打开相机，并计算预览尺寸
     * */
    private fun openCamera() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            sendStartStatus(false)
            throw SecurityException("no camera permission")
        }
        val cameraIdList = cameraManager.cameraIdList
        if (cameraIdList.isEmpty()) {
            sendStartStatus(false)
            return
        }
        val cameraId = CameraUtils.getCameraId(cameraManager, CameraCharacteristics.LENS_FACING_BACK)
        if (cameraId == null) {
            sendStartStatus(false)
            return
        }
        val videoSize = CameraUtils.chooseVideoSize(cameraManager, cameraId, MediaRecorder::class.java)
        previewSize = CameraUtils.chooseOptimalSize(cameraManager, cameraId, SurfaceTexture::class.java, width, height, videoSize)
        previewSize?.also {
            if (context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                preview.setAspectRation(it.width, it.height)
            } else {
                preview.setAspectRation(it.height, it.width)
            }
        }
        configureTransform(width, height)
        cameraManager.openCamera(cameraId, object : CameraDevice.StateCallback() {
            @SuppressLint("Recycle")
            override fun onOpened(camera: CameraDevice) {
                this@PreviewManager.camera = camera
//                mainHandler.obtainMessage(MSG_START_PREVIEW).sendToTarget()
                sendStartStatus(true)
            }

            override fun onDisconnected(camera: CameraDevice) {
                sendStartStatus(false)
                release()
            }

            override fun onError(camera: CameraDevice, error: Int) {
                sendStartStatus(false)
                release()
            }
        }, cameraHandler)
    }

    /**
     * 开启预览页面
     * */
    private fun startPreview() {
        releasePreview()
        previewSurfaceTexture = preview.surfaceTexture
        previewSize?.apply {
            previewSurfaceTexture?.setDefaultBufferSize(width, height)
        }
        previewSurface = Surface(previewSurfaceTexture)
        captureRequestBuilder = camera?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)?.apply {
            addTarget(previewSurface!!)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val sessionConfiguration = SessionConfiguration(
                SessionConfiguration.SESSION_REGULAR, listOf(OutputConfiguration(previewSurface!!)),
                CameraExecutor(cameraHandler),
                sessionStateCallback,
            )
            camera?.createCaptureSession(sessionConfiguration)
        } else {
            camera?.createCaptureSession(listOf(previewSurface), sessionStateCallback, cameraHandler)
        }
    }

    /**
     * 开启录像
     * */
    fun startRecording(surface: Surface) {
        releasePreview()
        this.videoSurface = surface
        //创建预览surface，如果只设置media recorder的surface，录像内容会只有黑屏（录像必须设置预览surface，否则无法接收camera信息）
        previewSurfaceTexture = preview.surfaceTexture
        previewSize?.apply {
            previewSurfaceTexture?.setDefaultBufferSize(width, height)
        }
        previewSurface = Surface(previewSurfaceTexture)

        captureRequestBuilder = camera?.createCaptureRequest(CameraDevice.TEMPLATE_RECORD)?.apply {
            addTarget(previewSurface!!)
            addTarget(videoSurface!!)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val sessionConfiguration = SessionConfiguration(
                SessionConfiguration.SESSION_REGULAR, listOf(OutputConfiguration(previewSurface!!), OutputConfiguration(videoSurface!!)),
                CameraExecutor(cameraHandler),
                sessionStateCallback,
            )
            camera?.createCaptureSession(sessionConfiguration)
        } else {
            camera?.createCaptureSession(listOf(previewSurface, videoSurface), sessionStateCallback, cameraHandler)
        }
        configureTransform(preview.width, preview.height)
    }

    /**
     * 停止录像
     * */
    fun stopRecording() {
        startPreview()
    }

    private val sessionStateCallback = object : CameraCaptureSession.StateCallback() {
        override fun onConfigured(session: CameraCaptureSession) {
            this@PreviewManager.session = session
            captureRequestBuilder?.build()?.also {
                session.setRepeatingRequest(it, null, cameraHandler)
            }
        }

        override fun onConfigureFailed(session: CameraCaptureSession) {
            release()
        }
    }

    private fun configureTransform(viewWidth: Int, viewHeight: Int) {
        if (previewSize != null && context is Activity) {
            val rotation = context.windowManager.defaultDisplay.rotation
            val matrix = Matrix()
            val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
            val bufferRect = RectF(0f, 0f, previewSize!!.height.toFloat(), previewSize!!.width.toFloat())
            val centerX = viewRect.centerX()
            val centerY = viewRect.centerY()
            if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
                bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
                matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
                val scale = (viewHeight.toFloat() / previewSize!!.height).coerceAtLeast(viewWidth.toFloat() / previewSize!!.width)
                with(matrix) {
                    postScale(scale, scale, centerX, centerY)
                    postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)
                }
            }
            preview.setTransform(matrix)
        }
    }

    private fun releasePreview() {
        /*previewSurfaceTexture?.release()
        previewSurface?.release()
        videoSurface?.release()
        captureRequestBuilder = null
        session?.close()*/
    }

    fun release() {
        releasePreview()
        camera?.close()
        cameraHandler.removeCallbacksAndMessages(null)
        mainHandler.removeCallbacksAndMessages(null)
        container?.removeAllViews()
    }
}