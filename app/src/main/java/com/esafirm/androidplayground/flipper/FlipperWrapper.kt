package com.esafirm.androidplayground.flipper

import android.app.Application
import com.esafirm.androidplayground.BuildConfig
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import java.io.File


object FlipperWrapper {

    private val networkPlugin = NetworkFlipperPlugin()

    fun createInterceptor() = FlipperOkhttpInterceptor(networkPlugin, true)

    fun setup(app: Application) {
        SoLoader.init(app, false)
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(app)) {
            AndroidFlipperClient.getInstance(app).apply {
                addPlugin(InspectorFlipperPlugin(app, DescriptorMapping.withDefaults()))
                addPlugin(CrashReporterPlugin.getInstance())
                addPlugin(DatabasesFlipperPlugin(app))
                addPlugin(networkPlugin)

                getAllSharedPreferences(app).forEach {
                    addPlugin(SharedPreferencesFlipperPlugin(app, it))
                }
            }.start()
        }
    }

    private fun getAllSharedPreferences(app: Application): List<String> {
        val prefsdir = File(app.applicationInfo.dataDir, "shared_prefs")
        if (prefsdir.exists() && prefsdir.isDirectory) {
            return prefsdir.list().map { it.removeSuffix(".xml") }.toList()
        }
        return emptyList()
    }

}