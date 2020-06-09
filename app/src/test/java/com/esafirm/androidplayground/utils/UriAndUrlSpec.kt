package com.esafirm.androidplayground.utils

import android.net.Uri
import io.kotlintest.specs.StringSpec
import java.net.URL

class UriAndUrlSpec: StringSpec({

    val urlString = "https://api.bukalapak.com/barang/1?id=1000"

   "Uri spec" {
       val uri = Uri.parse(urlString)
       println(uri.path)
       println(uri.scheme)
       println(uri.host)
   }

    "Url spec" {
        val url = URL(urlString)
        println(url.path)
        println(url.protocol)
        println(url.host)
    }
})