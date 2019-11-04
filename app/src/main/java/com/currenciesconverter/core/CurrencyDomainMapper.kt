package com.currenciesconverter.core

import com.currenciesconverter.data.CurrenciesDTO

interface CurrencyDomainMapper {
    fun map(data: CurrenciesDTO): List<CurrencyDomain>
}

class CurrencyDomainMapperDefault : CurrencyDomainMapper {
    override fun map(data: CurrenciesDTO): List<CurrencyDomain> {
        val currencies = mutableListOf<CurrencyDomain>()
        data.rates.map {
            val currencyDomain = CurrencyDomain(
                it.key,
                null,
                null,
                it.value,
                -1
            )
            currencies.add(currencyDomain)
        }
        return currencies
    }


}