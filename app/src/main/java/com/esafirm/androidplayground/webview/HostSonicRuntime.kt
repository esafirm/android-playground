package com.esafirm.androidplayground.webview

import android.content.Context
import android.os.Environment

import com.tencent.sonic.sdk.SonicRuntime
import com.tencent.sonic.sdk.SonicSessionClient

import java.io.File
import java.io.InputStream

class HostSonicRuntime(context: Context) : SonicRuntime(context) {

    override fun log(tag: String, level: Int, message: String) {

    }

    override fun getCookie(url: String): String? {
        return null
    }

    override fun setCookie(url: String, cookies: List<String>): Boolean {
        return false
    }

    /**
     * @return User's UA
     */
    override fun getUserAgent(): String {
        return ""
    }

    /**
     * @return the ID of user.
     */
    override fun getCurrentUserAccount(): String {
        return ""
    }

    override fun isSonicUrl(url: String): Boolean {
        return false
    }

    override fun createWebResourceResponse(mimeType: String, encoding: String, data: InputStream, headers: Map<String, String>): Any? {
        return null
    }

    override fun isNetworkValid(): Boolean {
        return false
    }

    override fun showToast(text: CharSequence, duration: Int) {

    }

    override fun postTaskToThread(task: Runnable, delayMillis: Long) {

    }

    override fun notifyError(client: SonicSessionClient, url: String, errorCode: Int) {}

    /**
     * @return the file path which is used to save Sonic caches.
     */
    override fun getSonicCacheDir(): File {
        val path = Environment.getExternalStorageDirectory().absolutePath + File.separator + "sonic/"
        val file = File(path.trim { it <= ' ' })
        if (!file.exists()) {
            file.mkdir()
        }
        return file
    }
}