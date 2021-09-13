package com.esafirm.androidplayground.network

import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

/**
 * This turns out to be just a desktop playground
 */
fun main() {
    val cache = File("/Users/esafirman/Desktop", "http_cache")
    if (cache.exists().not()) {
        cache.mkdirs()
    }

    val client = OkHttpClient.Builder()
            .cache(Cache(
                    directory = File("/Users/esafirman/Desktop", "http_cache"),
                    maxSize = 50L * 1024L * 1024L // 50 MiB
            ))
            .build()

    client.request("Calling #1")
    client.request("Calling #1")


    Thread.sleep(500)
}

private fun OkHttpClient.request(id: String) {
    println(id)

    val request = Request.Builder()
            .url("http://localhost:8080/")
            .header("X-Custom-Id", id)
            .build()

    val res = newCall(request).execute()

    println("headers: ${res.headers.toMap()}")
    println("body: ${res.body?.string()}")
    println("""
        cache res: ${res.cacheResponse}
        network res: ${res.networkResponse}
    """.trimIndent())
}