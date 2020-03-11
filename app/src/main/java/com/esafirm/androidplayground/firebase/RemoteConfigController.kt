package com.esafirm.androidplayground.firebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class RemoteConfigController : BaseController() {

    data class State(
        val currentConfig: String
    )

    private var currentState: State = State("")

    @Synchronized
    private fun setState(newState: State) {
        currentState = newState
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            input("Set your config here…") {
                setState(State(
                    currentConfig = it
                ))
            }
            button("Get Config") { getConfig() }
            logger()
        }
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        getRemoteConfig()
    }

    private fun getConfig() = logMeasure("GetConfig()") {
        val currentConfig = currentState.currentConfig
        Logger.log("Getting config ${currentState.currentConfig}")
        Logger.log(Firebase.remoteConfig[currentConfig].asString())
    }

    private fun getRemoteConfig() = logMeasure("GetRemoteConfig()") {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        })
        remoteConfig.setDefaultsAsync(
            mapOf(
                "TEST" to "1",
                "TEST2" to 2
            )
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Logger.log("Config params updated: $updated")
                Logger.log("Fetch and activate succeeded")
            } else {
                Logger.log("Fetch and activate failed")
            }
        }
    }
}