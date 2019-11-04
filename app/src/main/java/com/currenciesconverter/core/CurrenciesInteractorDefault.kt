package com.currenciesconverter.core


import com.currenciesconverter.data.CurrenciesRepository

internal class CurrenciesInteractorDefault(
    private val repository: CurrenciesRepository,
    private val mapper: CurrencyDomainMapper
) : CurrenciesContract.Interactor {
    override suspend fun getAllCurrencies(base: String, amount: Float?): CurrenciesDomain =
        CurrenciesDomain.Valid(
            mapper.map(repository.getRates(base, amount))
        )
}
