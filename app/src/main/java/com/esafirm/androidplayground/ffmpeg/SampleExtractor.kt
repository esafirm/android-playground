package com.esafirm.androidplayground.ffmpeg

import android.content.Context
import android.os.Environment
import com.esafirm.androidplayground.R
import java.io.File

class SampleExtractor(private val context: Context) {

    private val samples = arrayOf(
        R.raw.lizard to "lizard.png",
        R.raw.p to "p.gif"
    )

    private fun getProjectRoot(): File? {
        val folder = File(Environment.getExternalStorageDirectory(), "ap/")
        if (!folder.exists()) folder.mkdirs()
        return folder
    }

    private fun extractSampleFile(rawAndFilePair: Pair<Int, String>) {
        val (raw, name) = rawAndFilePair
        val destination = File(getProjectRoot(), name)
        if (destination.exists()) {
            return
        }

        destination.createNewFile()
        val inputStream = context.resources.openRawResource(raw)
        val outputStream = destination.outputStream()

        try {
            inputStream.copyTo(outputStream)
        } finally {
            inputStream.close()
            outputStream.close()
        }
    }

    fun execute() {
        samples.forEach {
            extractSampleFile(it)
        }
    }
}