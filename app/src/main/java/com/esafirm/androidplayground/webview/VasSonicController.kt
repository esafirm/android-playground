package com.esafirm.androidplayground.webview

import android.annotation.TargetApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.matchParent
import com.esafirm.androidplayground.utils.row
import com.esafirm.androidplayground.utils.webView
import com.tencent.sonic.sdk.SonicConfig
import com.tencent.sonic.sdk.SonicEngine
import com.tencent.sonic.sdk.SonicSession
import com.tencent.sonic.sdk.SonicSessionConfig

class VasSonicController : BaseController() {

    companion object {
        const val WEBVIEW_ID = 111
    }

    private lateinit var sonicSession: SonicSession
    private val getWebView get() = activity?.findViewById<WebView>(WEBVIEW_ID)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            logger()
            webView().apply {
                id = WEBVIEW_ID
            }
        }
    }

    private val application get() = requiredContext.application

    override fun onAttach(view: View) {
        super.onAttach(view)

        // step 1: Initialize sonic engine if necessary, or maybe u can do this when application created
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(HostSonicRuntime(application), SonicConfig.Builder().build())
        }


        val sonicSessionClient by lazy { SonicSessionClientImpl() }

        // step 2: Create SonicSession
        sonicSession = SonicEngine.getInstance()
                .createSession("URL", SonicSessionConfig.Builder().build())
        sonicSession.bindClient(sonicSessionClient)

        val webView = getWebView
        checkNotNull(webView) { "WebView cannot be null" }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                sonicSession.sessionClient.pageFinish(url)
            }

            @TargetApi(21)
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                return shouldInterceptRequest(view, request.url.toString())
            }

            override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
                return if (sonicSession != null) {
                    //step 6: Call sessionClient.requestResource when host allow the application
                    // to return the local data .
                    sonicSession.sessionClient.requestResource(url) as WebResourceResponse
                } else null
            }
        }

        val webSettings = webView.settings

        // step 4: bind javascript
        // note:if api level lower than 17(android 4.2), addJavascriptInterface has security
        // issue, please use x5 or see https://developer.android.com/reference/android/webkit/
        // WebView.html#addJavascriptInterface(java.lang.Object, java.lang.String)
        webSettings.javaScriptEnabled = true
        webView.removeJavascriptInterface("searchBoxJavaBridge_")
        webView.addJavascriptInterface(SonicJavaScriptInterface(sonicSessionClient, activity!!.intent), "sonic")

        // init webview settings
        webSettings.allowContentAccess = true
        webSettings.databaseEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
        webSettings.savePassword = false
        webSettings.saveFormData = false
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true


        // step 5: webview is ready now, just tell session client to bind
        sonicSessionClient.bindWebView(webView)
        sonicSessionClient.clientReady()

    }
}
