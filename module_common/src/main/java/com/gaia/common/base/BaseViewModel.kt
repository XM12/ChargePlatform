package com.gaia.common.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaia.common.base.bean.BaseBean
import com.gaia.common.base.bean.BaseListBean
import com.gaia.common.constant.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

open class BaseViewModel : ViewModel() {

    /**
     * 统一处理状态码
     * */
    suspend fun <T : Any> request(call: suspend () -> BaseBean<T>): T? {
        var response: T? = null
        return withContext(Dispatchers.IO) {
            runCatching {
                call.invoke()
            }.onSuccess {
                response = when (it.errorCode) {
                    0 -> it.data
                    else -> null
                }
                Log.e(Constant.HTTP_TAG, "onSuccess $it")
            }.onFailure {
                it.printStackTrace()
                Log.e(Constant.HTTP_TAG, "onFailure ${Log.getStackTraceString(it)}")
            }
            return@withContext response
        }
    }

    suspend fun <T : Any> requestList(call: suspend () -> BaseListBean<T>): MutableList<T>? {
        var response: MutableList<T>? = null
        return withContext(Dispatchers.IO) {
            runCatching {
                call.invoke()
            }.onSuccess {
                response = when (it.errorCode) {
                    0 -> it.data
                    else -> null
                }
                Log.e(Constant.HTTP_TAG, "onSuccess $it")
            }.onFailure {
                it.printStackTrace()
                Log.e(Constant.HTTP_TAG, "onFailure ${Log.getStackTraceString(it)}")
            }
            return@withContext response
        }
    }

    /**
     * 创建主线程，设置超时机制，超时候会抛出TimeoutCancellationException异常
     * */
    fun launchUI(block: suspend () -> Unit) = viewModelScope.launch {
        try {
            withTimeout(15 * 1000) {
                block()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}