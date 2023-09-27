package com.gaia.basic.net

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient private constructor() {
//    private val service: RetrofitService

    companion object {
        private const val TAG = "RetrofitClient"
        private const val CONNECT_TIMEOUT = 30L
        private const val READ_TIMEOUT = 10L

        val instance: RetrofitClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Log.e(TAG, "初始化RetrofitClient")
            RetrofitClient()
        }
    }

    init {
        Log.e(TAG, "初始化Retrofit")
//        service = getService("", RetrofitService::class.java)
    }

    private fun create(url: String): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(url)
            client(getOkHttpClient())
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }.build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        }.build()
    }

    private fun <T> getService(url: String, service: Class<T>): T = create(url).create(service)

}