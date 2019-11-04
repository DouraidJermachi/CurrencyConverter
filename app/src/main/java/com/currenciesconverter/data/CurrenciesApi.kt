package com.currenciesconverter.data

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrenciesApi {
    @GET("latest")
    fun getRates(
        @Query("base") base: String,
        @Query("amount") amount: Float?
    ): Deferred<CurrenciesDTO>
}