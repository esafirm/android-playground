package com.esafirm.androidplayground.others

import android.Manifest
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.libs.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import java.io.File

class FileController : BaseController() {

    companion object {
        private const val RC_WRITE_FILE = 1
        private const val RC_READ_FILE = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            button("Write File") { requestAccessFile(RC_WRITE_FILE) }
            button("Read File") { requestAccessFile(RC_READ_FILE) }
            logger()
        }
    }

    private fun requestAccessFile(rcCode: Int) {
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), rcCode)
    }

    private fun readFile() {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(dir, "text.txt")
        if (file.exists().not()) {
            Logger.log("File not exist")
            return
        }
        val content = file.readText()
        Logger.log("Content: $content")
    }

    private fun writeFile() {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(dir, "text.txt")
        if (file.exists().not()) {
            file.createNewFile()
        }
        file.writeText("Hello World!")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RC_WRITE_FILE -> writeFile()
            RC_READ_FILE -> readFile()
            else -> throw IllegalStateException("Invalid request code")
        }
    }
}
