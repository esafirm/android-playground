package com.esafirm.androidplayground.network

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.network.services.HttpBin
import com.esafirm.androidplayground.network.services.response.HttpBinResponse
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class ResponseCacheController : BaseController() {

    private val request by lazy {
        Net.getCacheService(requiredContext, "https://httpbin.org/", HttpBin::class.java)
    }

    private val cacheRequest by lazy {
        Net.getCacheService(requiredContext, "https://httpbin.org/", HttpBin::class.java, true)
    }

    private val simpleCache by lazy {
        SharedPrefCache(requiredContext)
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            button("Request HTTP with Cache Control") {
                scope.launch {
                    val start = System.currentTimeMillis()
                    val res = request.getCache()
                    val isCache = res.raw().cacheResponse != null

                    measureTimeMillis {
                        val body = res.body()
                        if (body != null) {
                            simpleCache.set(body)
                        }
                    }.also {
                        Logger.log("Simple cache save took $it ms")
                    }

                    val delta = System.currentTimeMillis() - start

                    Logger.log("Request took $delta ms. Is cache response: $isCache")
                }
            }
            button("Request Force Cache Only") {
                scope.launch {
                    val start = System.currentTimeMillis()
                    val res = cacheRequest.getCache()
                    val isCache = res.raw().cacheResponse != null
                    val delta = System.currentTimeMillis() - start

                    Logger.log("Cache request took $delta. Is Cache response: $isCache")
                    Logger.log("Data: ${res.body()?.url}")
                }
            }
            button("Request From Simple Cache") {
                scope.launch {
                    val start = System.currentTimeMillis()
                    val cache = simpleCache.get()
                    val delta = System.currentTimeMillis() - start

                    Logger.log("Simple cache took $delta")
                    Logger.log("Data: ${cache?.url}")
                }
            }
            logger()
        }
    }
}

private interface SimpleCache {
    fun set(response: HttpBinResponse)
    fun get(): HttpBinResponse?
}


class SharedPrefCache(context: Context) : SimpleCache {

    companion object {
        private const val KEY_CACHE = "Key.Cache"
    }

    private val sharedPref by lazy { context.getSharedPreferences("SimpleSharedPref", Context.MODE_PRIVATE) }
    private val gson by lazy { Gson() }

    override fun set(response: HttpBinResponse) {
        sharedPref.edit()
            .putString(KEY_CACHE, gson.toJson(response))
            .apply()
    }

    override fun get(): HttpBinResponse? {
        val rawString = sharedPref.getString(KEY_CACHE, null)
        if (rawString != null) {
            return gson.fromJson(rawString, HttpBinResponse::class.java)
        }
        return null
    }

}
