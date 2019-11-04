package com.currenciesconverter.core

import com.currenciesconverter.app.CurrencyModel

interface CurrenciesContract {

    interface View {

        fun setItems(items: List<CurrencyModel>)

        fun showLoading(visible: Boolean)

        fun showError(errorMessage: String)

        fun setBaseCurrency(queryCurrency: CurrencyModel)

        fun hideKeyBoard()
    }

    interface Presenter {

        fun bindView(view: View)

        fun unbindView()

        fun init()

        fun onCurrencyClicked(currencyModel: CurrencyModel)

        fun onTypingQuery(charSequence: CharSequence)

        fun submitSearch(charSequence: CharSequence?)
    }

    interface Interactor {

        suspend fun getAllCurrencies(
            base: String,
            amount: Float?
        ): CurrenciesDomain
    }
}
