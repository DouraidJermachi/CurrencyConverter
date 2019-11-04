package com.currenciesconverter.core


internal class CurrenciesInteractorErrorHandling(
    private val interactor: CurrenciesContract.Interactor
) : CurrenciesContract.Interactor {
    override suspend fun getAllCurrencies(base: String, amount: Float?): CurrenciesDomain =
        try {
            interactor.getAllCurrencies(base, amount)
        } catch (e: Exception) {
            CurrenciesDomain.Invalid(e.message)
        }
}
