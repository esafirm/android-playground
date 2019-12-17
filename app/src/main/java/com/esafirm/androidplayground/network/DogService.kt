package com.esafirm.androidplayground.network

import retrofit2.http.GET

interface DogService {

    @GET("breeds/image/random")
    suspend fun getRandomDogs(): RandomDogImage
}