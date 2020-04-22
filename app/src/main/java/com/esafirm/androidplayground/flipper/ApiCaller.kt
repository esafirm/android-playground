package com.esafirm.androidplayground.flipper

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface DummyService {
    @GET("api/v1/employees")
    fun getEmployees(): Call<List<Employee>>
}

object ApiCaller {
    private val retrofit by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(FlipperWrapper.createInterceptor())
            .build()

        Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://dummy.restapiexample.com/")
            .build()
    }

    private val dummyService by lazy {
        retrofit.create(DummyService::class.java)
    }

    fun getEmployee(): Call<List<Employee>> {
        return dummyService.getEmployees()
    }
}