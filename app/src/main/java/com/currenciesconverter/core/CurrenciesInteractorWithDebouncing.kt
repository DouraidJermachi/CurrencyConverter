package com.currenciesconverter.core

import kotlinx.coroutines.delay

class CurrenciesInteractorWithDebouncing(
    private val interactor: CurrenciesContract.Interactor,
    private val delayInMilliseconds: Long = 100
) : CurrenciesContract.Interactor by interactor {

    override suspend fun getAllCurrencies(base: String, amount: Float?): CurrenciesDomain {
        delay(delayInMilliseconds)
        return interactor.getAllCurrencies(base, amount)
    }

}
