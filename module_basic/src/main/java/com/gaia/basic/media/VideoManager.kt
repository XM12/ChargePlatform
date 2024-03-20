package com.gaia.basic.media

import android.content.Context
import android.hardware.camera2.CameraManager
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import android.util.Size
import com.gaia.basic.utils.CameraUtils
import java.io.File

@Suppress("DEPRECATION")
class VideoManager(private val context: Context, private val previewManager: PreviewManager) {

    companion object {
        private const val TAG = "VideoManager"
    }

    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var mediaRecorder: MediaRecorder? = null
    private var nextVideoAbsolutePath: String? = null
    private var isRecording = false
    private var width = 0
    private var height = 0
    private var videoPath: String? = null

    private fun initMediaRecorder() {
        val cameraId = previewManager.getCameraId()
        if (cameraId != null) {
            CameraUtils.chooseVideoSize(cameraManager, cameraId, MediaRecorder::class.java)?.let {
                width = it.width
                height = it.height
            }
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                MediaRecorder()
            }
            nextVideoAbsolutePath = getVideoFilePath(videoPath)
            mediaRecorder!!.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setVideoSource(MediaRecorder.VideoSource.SURFACE)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setVideoEncoder(MediaRecorder.VideoEncoder.H264)
                setVideoSize(width, height)
                setVideoFrameRate(30)
                setOutputFile(nextVideoAbsolutePath)
                setVideoEncodingBitRate(10000000)
                setMaxDuration(10 * 1000)
                prepare()
            }
            previewManager.startRecording(mediaRecorder!!.surface)
        }
    }

    fun startLoopRecording() {
        initMediaRecorder()
        mediaRecorder?.start()
        isRecording = true
        mediaRecorder?.setOnInfoListener { mr, what, extra ->
            if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                Log.d(TAG, "startLoopRecording: 10s recording end, restart recording")
                releaseRecorder()
                startLoopRecording()
            }
        }
    }

    fun releaseRecorder() {
        try {
            mediaRecorder?.setOnErrorListener(null)
            mediaRecorder?.setOnInfoListener(null)
            mediaRecorder?.setPreviewDisplay(null)
            mediaRecorder?.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaRecorder?.reset()
            mediaRecorder?.release()
            mediaRecorder = null
            isRecording = false
        }
    }

    private fun getVideoFilePath(videoPath: String?): String {
        val filename = "${System.currentTimeMillis()}.mp4"
        return "${videoPath ?: context.getExternalFilesDir("")}${File.separator}$filename"
    }

}