package com.currenciesconverter.core

internal sealed class CurrenciesEvent {

    data class Init(val baseModel: CurrencyModel) : CurrenciesEvent()

    data class Fetch(val base: String, val amount: Float) : CurrenciesEvent()

    data class OnCurrencySelected(
        val selectedCurrencyModel: CurrencyModel
    ) : CurrenciesEvent()

    data class OnTyping(
        val amount: String
    ) : CurrenciesEvent()

    data class UpdateUi(
        val models: List<CurrencyModel>,
        val selectedModel: CurrencyModel? = null
    ) : CurrenciesEvent()

    data class ShowError(
        val networkErrorMessage: String? = null,
        val selectedModel: CurrencyModel? = null
    ) : CurrenciesEvent()
}
