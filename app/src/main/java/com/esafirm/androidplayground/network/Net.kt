package com.esafirm.androidplayground.network

import android.content.Context
import com.pandulapeter.beagle.BeagleNetworkInterceptor
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Net {

    const val BASE_URL = "https://dog.ceo/api/"

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(BeagleNetworkInterceptor)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val cacheInterceptor by lazy {
        object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val cacheRequest: Request = chain.request().newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()

                return chain.proceed(cacheRequest)
            }
        }
    }

    fun <T> getCacheService(
        context: Context,
        baseUrl: String,
        service: Class<T>,
        forceCache: Boolean = false
    ): T {
        val cache = Cache(
            context.cacheDir,
            maxSize = 50L * 1024L * 1024L
        )

        val retrofit = Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .cache(cache)
                    .apply {
                        if (forceCache) {
                            addInterceptor(cacheInterceptor)
                        }
                    }
                    .build()
            )
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(service)
    }

    fun <T> getService(service: Class<T>): T {
        return retrofit.create(service)
    }
}
