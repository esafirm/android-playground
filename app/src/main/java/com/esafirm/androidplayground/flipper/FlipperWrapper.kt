package com.esafirm.androidplayground.flipper

import android.app.Application
import com.esafirm.androidplayground.BuildConfig
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader

object FlipperWrapper {

    private val networkPlugin = NetworkFlipperPlugin()

    fun createInterceptor() = FlipperOkhttpInterceptor(networkPlugin)

    fun setup(app: Application) {
        SoLoader.init(app, false)
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(app)) {
            AndroidFlipperClient.getInstance(app).apply {
                addPlugin(InspectorFlipperPlugin(app, DescriptorMapping.withDefaults()))
                addPlugin(SharedPreferencesFlipperPlugin(app, "playground"))
                addPlugin(networkPlugin)
            }.start()
        }
    }
}