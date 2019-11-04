package com.currenciesconverter.data

interface CurrenciesRepository {
    suspend fun getRates(base: String, amount: Float?): CurrenciesDTO
}