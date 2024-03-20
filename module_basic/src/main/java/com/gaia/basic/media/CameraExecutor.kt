package com.gaia.basic.media

import android.os.Handler
import java.util.concurrent.Executor

class CameraExecutor(private val handler: Handler) : Executor {

    override fun execute(command: Runnable?) {
        command?.let {
            handler.post(command)
        }
    }
}