package com.currenciesconverter.data


class CurrenciesRepositoryDefault(
    private val currenciesApi: CurrenciesApi
) : CurrenciesRepository {

    override suspend fun getRates(base: String, amount: Float?): CurrenciesDTO {
        return currenciesApi.getRates(base, amount).await()
    }
}