package com.esafirm.androidplayground

import android.app.Application
import com.esafirm.androidplayground.network.Net
import com.esafirm.androidplayground.utils.ProcessUtils
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagleCore.configuration.Trick


object BeagleInitializer {
    fun initialize(app: Application) {
        if (ProcessUtils.isMainProcess(app)) return

        Beagle.imprint(app)
        Beagle.learn(
            listOf(
                Trick.Header(
                    title = app.getString(R.string.app_name),
                    subtitle = "${BuildConfig.VERSION_NAME} ${BuildConfig.VERSION_CODE})"
                ),
                Trick.DeviceInformationKeyValue(),
                Trick.NetworkLogList(
                    baseUrl = Net.BASE_URL
                )
            )
        )
    }
}