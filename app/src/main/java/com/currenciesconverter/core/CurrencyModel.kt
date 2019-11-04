package com.currenciesconverter.core

data class CurrencyModel(
    val code: String,
    val name: String?,
    val symbol: String?,
    val amount: String?,
    val logo: Int
)