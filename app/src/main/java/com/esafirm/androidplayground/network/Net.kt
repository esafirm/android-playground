package com.esafirm.androidplayground.network

import com.pandulapeter.beagle.BeagleNetworkInterceptor
import okhttp3.OkHttpClient
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

    fun <T> getService(service: Class<T>): T {
        return retrofit.create(service)
    }
}