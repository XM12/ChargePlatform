package com.pinyou.basic.net

import com.pinyou.basic.BuildConfig
import com.pinyou.basic.api.ForumApi
import com.pinyou.basic.api.PersonalPileApi
import com.pinyou.basic.api.PublicPileApi
import com.pinyou.basic.api.UserApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient private constructor() {
    private val publishedApi: PublicPileApi
    private val forumApi: ForumApi
    private val personalPileApi: PersonalPileApi
    private val userApi: UserApi

    companion object {
        private const val TAG = "RetrofitClient"
        private const val CONNECT_TIMEOUT = 30L
        private const val READ_TIMEOUT = 10L

        val instance: ApiClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiClient()
        }
    }

    init {
        publishedApi = getService(BuildConfig.BASE_URL, PublicPileApi::class.java)
        forumApi = getService(BuildConfig.BASE_URL, ForumApi::class.java)
        personalPileApi = getService(BuildConfig.BASE_PERSONAL_URL, PersonalPileApi::class.java)
        userApi = getService(BuildConfig.BASE_URL, UserApi::class.java)
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