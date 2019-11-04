package com.currenciesconverter.app

data class CurrencyModel(
    val code: String,
    val name: String?,
    val symbol: String?,
    val amount: String?,
    val logo: Int
)