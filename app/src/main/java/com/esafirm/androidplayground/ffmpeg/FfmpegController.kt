package com.esafirm.androidplayground.ffmpeg

import android.Manifest
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.input
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler
import nl.bravobit.ffmpeg.FFmpeg
import nl.bravobit.ffmpeg.FFtask

class FfmpegController : BaseController() {

    companion object {
        private const val RC_STORAGE = 1
    }

    data class State(
        val command: String
    )

    private var currentState: State = State("")

    private var currentTask: FFtask? = null

    @Synchronized
    private fun setState(newState: State) {
        currentState = newState
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        requestPermissions(
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            RC_STORAGE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == RC_STORAGE) {
            SampleExtractor(requiredContext).execute()
            Logger.log("Sample extracted…")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            input { text ->
                setState(State(text))
            }
            button("Run Command") {
                runCommand()
            }
            button("Get Current Task") {
                printCurrentTaskInfo()
            }
            button("Send Quit Signal") {
                currentTask?.sendQuitSignal()
            }
            logger()
        }
    }

    private fun printCurrentTaskInfo() {
        if (currentTask == null) {
            Logger.log("Current task is null…")
            return
        }
        val log = """
            Is task completed: ${currentTask?.isProcessCompleted}
            Is command running: ${FFmpeg.getInstance(requiredContext).isCommandRunning(currentTask)}
        """.trimIndent()
        Logger.log(log)
    }

    private fun runCommand() {
        if (FFmpeg.getInstance(requiredContext).isSupported.not()) {
            throw IllegalStateException("FFmpeg is not supported!")
        }

        val commandArray = currentState.command
            .split(" ")
            .filter { it.isNotBlank() }
            .toTypedArray()

        val ffmpeg = FFmpeg.getInstance(requiredContext)
        currentTask = ffmpeg.execute(commandArray, object : ExecuteBinaryResponseHandler() {
            override fun onStart() {
                Logger.log("onStart")
            }

            override fun onProgress(message: String?) {
                Logger.log("onProgress: $message")
            }

            override fun onFailure(message: String?) {
                Logger.divider()
                Logger.log("onFailure: $message")
            }

            override fun onSuccess(message: String?) {
                Logger.divider()
                Logger.log("onSuccess: $message")
            }

            override fun onFinish() {
                Logger.log("onFinish")
            }
        })
    }

}