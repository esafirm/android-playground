package com.esafirm.androidplayground.network.services

import com.esafirm.androidplayground.network.RandomDogImage
import retrofit2.http.GET

interface DogService {

    @GET("breeds/image/random")
    suspend fun getRandomDogs(): RandomDogImage
}
