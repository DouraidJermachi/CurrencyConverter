package com.currenciesconverter.app

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import com.currenciesconverter.core.*
import com.currenciesconverter.data.CurrenciesApi
import com.currenciesconverter.data.CurrenciesRepository
import com.currenciesconverter.data.CurrenciesRepositoryDefault
import com.currenciesconverter.utils.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal class CurrenciesServiceLocator(private val context: Context) {

    fun getPresenter(): CurrenciesContract.Presenter {
        val interactor = getCurrenciesInteractorMergingFlagsAndNames()
        val coroutineDispatcherFactory = getCoroutineDispatcherFactory()
        val modelMapper = getCurrencyModelMapper()
        val strings = getStringResourceWrapper()
        return CurrenciesPresenter(interactor, coroutineDispatcherFactory, modelMapper, strings)
    }

    private fun getCoroutineDispatcherFactory(): CoroutineDispatcherFactory =
        CoroutineDispatcherFactoryDefault()

    private fun getCurrenciesInteractorMergingFlagsAndNames(): CurrenciesContract.Interactor {
        val baseInteractor = getCurrenciesInteractorWithDebouncing()
        return CurrenciesInteractorMergingFlagsAndNames(baseInteractor)
    }

    private fun getCurrenciesInteractorWithDebouncing(): CurrenciesContract.Interactor {
        val baseInteractor = getCurrenciesInteractorErrorHandling()
        return CurrenciesInteractorWithDebouncing(baseInteractor)
    }

    private fun getCurrenciesInteractorErrorHandling(): CurrenciesContract.Interactor {
        val baseInteractor = getCurrenciesInteractorDefault()
        return CurrenciesInteractorErrorHandling(baseInteractor)
    }

    private fun getCurrenciesInteractorDefault(): CurrenciesContract.Interactor {
        val repository = getCurrenciesRepository()
        val mapper = getCurrencyDomainMapper()
        return CurrenciesInteractorDefault(repository, mapper)
    }

    private fun getCurrencyDomainMapper() = CurrencyDomainMapperDefault()
    private fun getCurrencyModelMapper() = CurrencyModelMapperDefault()

    private fun getCurrenciesRepository(): CurrenciesRepository {

        val retrofit = getRetrofit()
        val ratesApi = getApi(retrofit)
        return CurrenciesRepositoryDefault(ratesApi)
    }

    private fun getApi(retrofit: Retrofit): CurrenciesApi =
        retrofit.create(CurrenciesApi::class.java)

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    private fun getStringResourceWrapper(): StringResourceWrapper =
        StringResourceWrapperDefault(context)

    fun getCurrenciesAdabter(presenter: CurrenciesContract.Presenter): CurrenciesAdapter {
        return CurrenciesAdapter(
            object : CurrenciesAdapter.Listener {
                override fun onCLick(currencyModel: CurrencyModel) {
                    presenter.onCurrencyClicked(currencyModel)
                }
            },
            CurrenciesViewHolderFactory()
        )
    }

    fun getTextWatcher(presenter: CurrenciesContract.Presenter): TextWatcher {

        return object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                presenter.onTypingQuery(charSequence)
            }

            override fun afterTextChanged(editable: Editable) {}
        }
    }

    fun getNewAndroidUtils() = NewAndroidUtils(context)

    companion object {
        private const val baseURL = "https://revolut.duckdns.org/"
    }
}
