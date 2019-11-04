package com.currenciesconverter.core


import com.currenciesconverter.R
import com.currenciesconverter.utils.CoroutineDispatcherFactory
import com.currenciesconverter.utils.StringResourceWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

internal class CurrenciesPresenter(
    private val interactor: CurrenciesContract.Interactor,
    private val dispatcherFactory: CoroutineDispatcherFactory,
    private val modelMapper: CurrencyModelMapper,
    private val strings: StringResourceWrapper
) : CurrenciesContract.Presenter, CoroutineScope {

    private val channel = Channel<CurrenciesEvent>(Channel.RENDEZVOUS)

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = dispatcherFactory.UIDispatcher + job

    private var view: CurrenciesContract.View? = null

    private var currenciesModel = emptyList<CurrencyModel>()

    override fun bindView(view: CurrenciesContract.View) {
        this.view = view

        val uiReceiveChannel = handleUserEvents(channel)
        handleUiEvents(uiReceiveChannel)
    }

    override fun unbindView() {
        this.view = null

        coroutineContext.cancelChildren()
        channel.close()
        job.cancel()
    }

    override fun init() {
        view?.showLoading(true)
        sendEvent(CurrenciesEvent.Init(modelMapper.mapToModel(DOMAIN_EUR)))
        sendEvent(CurrenciesEvent.Fetch(EUR, AMOUNT1.toFloat()))
    }

    override fun onCurrencyClicked(currencyModel: CurrencyModel) {
        view?.showLoading(true)
        sendEvent(CurrenciesEvent.OnCurrencySelected(currencyModel))
    }

    override fun onTypingQuery(charSequence: CharSequence) {
        sendEvent(CurrenciesEvent.OnTyping(charSequence.toString()))
    }

    override fun submitSearch(charSequence: CharSequence?) {
        view?.let {
            it.showLoading(true)
            it.hideKeyBoard()
        }
        sendEvent(CurrenciesEvent.OnTyping(charSequence.toString()))
    }

    private fun replaceWithZeroAmount(): List<CurrencyModel> {
        val copyModels = mutableListOf<CurrencyModel>()
        currenciesModel.map {
            copyModels.add(
                it.copy(amount = AMOUNT0)
            )
        }
        return copyModels
    }

    private fun CoroutineScope.sendEvent(event: CurrenciesEvent) =
        launch(dispatcherFactory.UIDispatcher) {
            channel.send(event)
        }

    private fun CoroutineScope.handleUserEvents(
        receiveChannel: ReceiveChannel<CurrenciesEvent>
    ) = produce(dispatcherFactory.IODispatcher, Channel.RENDEZVOUS) {

        var queryCurrency = DOMAIN_EUR

        for (event in receiveChannel) {
            try {
                when (event) {
                    is CurrenciesEvent.Fetch -> {
                        val domains = interactor.getAllCurrencies(event.base, event.amount)
                        when (domains) {
                            is CurrenciesDomain.Valid -> {
                                send(CurrenciesEvent.UpdateUi(domains.currencies.map {
                                    modelMapper.mapToModel(
                                        it
                                    )
                                }))
                            }
                            is CurrenciesDomain.Invalid -> send(
                                CurrenciesEvent.ShowError(
                                    domains.errorMessag
                                )
                            )
                        }
                    }
                    is CurrenciesEvent.OnCurrencySelected -> {
                        queryCurrency = modelMapper.mapToDomain(event.selectedCurrencyModel)
                        val domains = interactor.getAllCurrencies(
                            event.selectedCurrencyModel.code,
                            event.selectedCurrencyModel.amount?.toFloat()
                        )
                        when (domains) {
                            is CurrenciesDomain.Valid -> {
                                send(CurrenciesEvent.UpdateUi(domains.currencies.map {
                                    modelMapper.mapToModel(it)
                                }, event.selectedCurrencyModel))
                            }
                            is CurrenciesDomain.Invalid -> send(
                                CurrenciesEvent.ShowError(
                                    domains.errorMessag,
                                    event.selectedCurrencyModel
                                )
                            )
                        }
                    }
                    is CurrenciesEvent.OnTyping -> {
                        var amount = AMOUNT0
                        try {
                            val amountF = event.amount.toFloat()
                            amount = amountF.toString()
                        } catch (e: Exception) {
                            //do nothing
                        }

                        if (amount == AMOUNT0) {
                            val zerosList = replaceWithZeroAmount()
                            send(CurrenciesEvent.UpdateUi(zerosList))
                        } else {

                            val domains = interactor.getAllCurrencies(
                                queryCurrency.code,
                                event.amount.toFloat()
                            )
                            when (domains) {
                                is CurrenciesDomain.Valid -> {
                                    send(CurrenciesEvent.UpdateUi(domains.currencies.map {
                                        modelMapper.mapToModel(it)
                                    }))
                                }
                                is CurrenciesDomain.Invalid -> send(
                                    CurrenciesEvent.ShowError(domains.errorMessag)
                                )
                            }
                        }
                    }
                    is CurrenciesEvent.Init -> send(event)
                }
            } catch (e: Exception) {
                send(CurrenciesEvent.ShowError())
            }
        }
    }


    private fun CoroutineScope.handleUiEvents(
        receiveChannel: ReceiveChannel<CurrenciesEvent>
    ) = launch(dispatcherFactory.UIDispatcher) {

        for (event in receiveChannel) {
            view?.let { view ->
                when (event) {
                    is CurrenciesEvent.Init -> {
                        view.setBaseCurrency(event.baseModel)
                    }

                    is CurrenciesEvent.UpdateUi -> {
                        view.showLoading(false)
                        event.selectedModel?.let {
                            view.setBaseCurrency(it)
                        }
                        if (event.models.isEmpty()) {
                            view.setItems(replaceWithZeroAmount())
                        } else {
                            currenciesModel = event.models
                            view.setItems(event.models)
                        }
                    }

                    is CurrenciesEvent.ShowError -> {
                        view.setItems(replaceWithZeroAmount())

                        view.showLoading(false)
                        event.selectedModel?.let {
                            view.setBaseCurrency(it)
                        }
                        if (event.networkErrorMessage != null) {
                            view.showError(event.networkErrorMessage)
                        } else {
                            view.showError(strings.getString(R.string.error_unknown))
                        }
                    }

                    else -> {
                        view.showLoading(false)
                        view.showError(strings.getString(R.string.error_unknown))
                    }
                }
            }
        }
    }

    companion object {
        private const val EUR = "EUR"
        private const val EURO = "Euro"
        private const val SYMBOL_EUR = "€"
        private const val AMOUNT1 = "1.0"
        private const val FLAG_EUR = R.drawable.flag_eur

        private const val AMOUNT0 = "0.0"

        private val DOMAIN_EUR = CurrencyDomain(
            EUR,
            EURO,
            SYMBOL_EUR,
            AMOUNT1,
            FLAG_EUR
        )
    }
}
