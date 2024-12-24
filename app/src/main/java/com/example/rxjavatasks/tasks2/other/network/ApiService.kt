package com.example.rxjavatasks.tasks2.other.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("character")
    fun getCharacters(
        @Query("page") page: Int = 1
    ): Single<CharacterResponse>
}