package com.esafirm.androidplayground.network.services

import com.esafirm.androidplayground.network.services.response.HttpBinResponse
import retrofit2.Response
import retrofit2.http.GET

interface HttpBin {

    /**
     * Cache response for 1000 seconds
     */
    @GET("cache/1000")
    suspend fun getCache(): Response<HttpBinResponse>
}
