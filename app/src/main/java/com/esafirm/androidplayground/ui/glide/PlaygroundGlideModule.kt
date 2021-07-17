package com.esafirm.androidplayground.ui.glide

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.InputStream
import java.util.concurrent.TimeUnit
import kotlin.system.measureTimeMillis

@GlideModule
class PlaygroundGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor { chain ->

                lateinit var response: Response
                val request = chain.request()
                measureTimeMillis {
                    response = chain.proceed(request)
                }.also {
                    GlideRequestTrackerStore.add(EventType.REQUEST, it)
                    Log.d("GlideMeasure", "Request ${request.url} took $it ms")
                }
                response
            }
            .build()

        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(client)
        )
    }
}