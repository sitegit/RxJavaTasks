package com.example.rxjavatasks.tasks2.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface ApiService {

    @GET("character")
    fun getNumberOfCharacters(): Single<CharacterResponse>
}