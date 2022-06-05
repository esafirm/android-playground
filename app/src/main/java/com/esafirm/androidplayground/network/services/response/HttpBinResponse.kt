package com.esafirm.androidplayground.network.services.response

data class HttpBinResponse(
        val headers: Map<String, String>,
        val origin: String,
        val url: String
)
