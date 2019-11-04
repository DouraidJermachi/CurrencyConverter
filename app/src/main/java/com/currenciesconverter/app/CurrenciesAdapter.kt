package com.currenciesconverter.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.currenciesconverter.R
import com.currenciesconverter.core.CurrencyModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.currency_element.view.*

internal class CurrenciesAdapter(
    private val listener: Listener,
    private val holderFactory: CurrenciesViewHolderFactory
) : RecyclerView.Adapter<CurrenciesViewHolder>() {

    private var currencies = emptyList<CurrencyModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesViewHolder =
        holderFactory.onCreateViewHolder(parent)

    override fun onBindViewHolder(viewHolder: CurrenciesViewHolder, position: Int) {
        viewHolder.bind(currencies[position], listener)
    }

    override fun getItemCount() = currencies.size

    fun setItems(currencies: List<CurrencyModel>) {
        this.currencies = currencies
    }

    interface Listener {
        fun onCLick(currencyModel: CurrencyModel)
    }
}

internal class CurrenciesViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(currencyModel: CurrencyModel, listener: CurrenciesAdapter.Listener) {

        Glide.with(containerView.context)
            .load(currencyModel.logo)
            .apply(RequestOptions().circleCrop())
            .into(containerView.avatarImageView)

        currencyModel.name?.let {
            containerView.currencyName.text = containerView.context.getString(
                R.string.currency_name_plus_symbol,
                it,
                currencyModel.symbol
            )
        }
        containerView.currencyCode.text = currencyModel.code

        currencyModel.amount?.let {
            containerView.amount.text = it
        }

        containerView.setOnClickListener {
            listener.onCLick(currencyModel)
        }
    }
}

internal class CurrenciesViewHolderFactory {

    fun onCreateViewHolder(parent: ViewGroup): CurrenciesViewHolder =
        parent.inflate(R.layout.currency_element).let { view ->
            CurrenciesViewHolder(view)
        }
}

private fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(resource, this, attachToRoot)