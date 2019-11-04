package com.currenciesconverter.core

sealed class CurrenciesDomain {
    class Valid(
        val currencies: List<CurrencyDomain>
    ) : CurrenciesDomain()

    class Invalid(
        val errorMessag: String?
    ) : CurrenciesDomain()
}

data class CurrencyDomain(
    val code: String,
    val name: String?,
    val symbol: String?,
    val amount: String?,
    val logo: Int
)