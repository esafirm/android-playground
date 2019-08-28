package com.esafirm.androidplayground.webview

import android.os.Bundle
import android.webkit.WebView

import com.tencent.sonic.sdk.SonicSessionClient

import java.util.HashMap

class SonicSessionClientImpl : SonicSessionClient() {

    lateinit var webView: WebView

    fun bindWebView(webView: WebView) {
        this.webView = webView
    }

    override fun loadUrl(url: String, extraData: Bundle) {
        webView.loadUrl(url)
    }

    override fun loadDataWithBaseUrl(
            baseUrl: String,
            data: String,
            mimeType: String,
            encoding: String,
            historyUrl: String
    ) {
        webView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl)
    }

    override fun loadDataWithBaseUrlAndHeader(
            baseUrl: String,
            data: String,
            mimeType: String,
            encoding: String,
            historyUrl: String,
            headers: HashMap<String, String>
    ) {
    }
}