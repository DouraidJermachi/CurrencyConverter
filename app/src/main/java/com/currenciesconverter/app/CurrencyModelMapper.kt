package com.currenciesconverter.app

import com.currenciesconverter.core.CurrencyDomain

interface CurrencyModelMapper {
    fun mapToModel(currencyDomain: CurrencyDomain): CurrencyModel
    fun mapToDomain(currencyModel: CurrencyModel):  CurrencyDomain
}

class CurrencyModelMapperDefault : CurrencyModelMapper {

    override fun mapToModel(currencyDomain: CurrencyDomain): CurrencyModel =
        CurrencyModel(
            currencyDomain.code,
            currencyDomain.name,
            currencyDomain.symbol,
            currencyDomain.amount,
            currencyDomain.logo
        )

    override fun mapToDomain(currencyModel: CurrencyModel): CurrencyDomain =
        CurrencyDomain(
            currencyModel.code,
            currencyModel.name,
            currencyModel.symbol,
            currencyModel.amount,
            currencyModel.logo
        )

}