package com.esafirm.androidplayground.network

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.Logger
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import okhttp3.FormProgressBody
import okhttp3.OkHttpClient
import okhttp3.Request

class UploadProgressController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            Logger.getLogView(container.context)

    override fun onAttach(view: View) {

        val body = FormProgressBody.Builder()
                .add("a", "test")
                .add("b", 50_000.generateString())
                .build()

        val countingBody = CountingRequestBody(body, CountingRequestBody.Listener { bytesWritten, contentLength ->
            val percentage = 100f * bytesWritten / contentLength
            Logger.log("Uploaded $percentage%")
        })

        val client = OkHttpClient.Builder()
                .build()

        val call = client.newCall(Request.Builder()
                .url("https://httpbin.org/post")
                .post(countingBody)
                .build())

        Logger.log("Starting…")

        Completable.fromAction { call.execute() }
                .subscribeOn(Schedulers.io())
                .subscribe { Logger.log("Request complete") }
    }

    private fun Int.generateString(): String {
        val stringBuilder = StringBuilder()
        for (i in 0..this) {
            stringBuilder.append(i)
        }
        return stringBuilder.toString();
    }
}
