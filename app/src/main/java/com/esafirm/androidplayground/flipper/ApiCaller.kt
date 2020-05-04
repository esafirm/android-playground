package com.esafirm.androidplayground.flipper

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface DummyService {
    @GET("api/v1/employees")
    fun getEmployees(): Call<List<Employee>>

    @POST("api/v1/employee")
    fun createEmployee(@Body employee: Employee): Call<Employee>
}

object ApiCaller {
    private val retrofit by lazy {
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(FlipperWrapper.createInterceptor())
            .addInterceptor(FlipperWrapper.createMockInterceptor())
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

    fun createEmployee(employee: Employee): Call<Employee> {
        return dummyService.createEmployee(employee)
    }
}