package com.currenciesconverter.app

import androidx.recyclerview.widget.DiffUtil
import com.currenciesconverter.core.CurrencyModel

internal class CurrenciesDiffUtil : DiffUtil.ItemCallback<CurrencyModel>() {

    override fun areContentsTheSame(oldItem: CurrencyModel, newItem: CurrencyModel): Boolean =
        oldItem == newItem

    override fun areItemsTheSame(oldItem: CurrencyModel, newItem: CurrencyModel): Boolean =
                oldItem.code == newItem.code
}