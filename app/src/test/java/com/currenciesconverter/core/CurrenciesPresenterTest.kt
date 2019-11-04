package com.currenciesconverter.core

import com.currenciesconverter.R
import com.currenciesconverter.app.CurrencyModel
import com.currenciesconverter.app.CurrencyModelMapper
import com.currenciesconverter.utils.CoroutineDispatcherFactory
import com.currenciesconverter.utils.CoroutineDispatcherFactoryUnconfined
import com.currenciesconverter.utils.StringResourceWrapper
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class CurrenciesPresenterTest {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var mockedInteractor: CurrenciesContract.Interactor

    private val mockedDF: CoroutineDispatcherFactory = CoroutineDispatcherFactoryUnconfined()

    @Mock
    private lateinit var mockedModelMapper: CurrencyModelMapper

    @Mock
    private lateinit var mockedStringResourceWrapper: StringResourceWrapper

    @Mock
    private lateinit var mockedView: CurrenciesContract.View

    private lateinit var presenter: CurrenciesContract.Presenter

    @Before
    fun setUp() {
        presenter = CurrenciesPresenter(
            mockedInteractor,
            mockedDF,
            mockedModelMapper,
            mockedStringResourceWrapper
        )

        presenter.bindView(mockedView)

        Mockito.doReturn(GENERIC_ERROR_MESSAGE).`when`(mockedStringResourceWrapper)
            .getString(ArgumentMatchers.anyInt())

        Mockito.doReturn(modelEUR).`when`(mockedModelMapper)
            .mapToModel(domainEUR)
        Mockito.doReturn(modelGBP).`when`(mockedModelMapper)
            .mapToModel(domainGBP)
    }

    @Test
    fun `when landing on the screen, then do set the original currency`() {
        // given

        Mockito.doReturn(modelEUR).`when`(mockedModelMapper)
            .mapToModel(domainEUR)
        // when

        presenter.init()

        // then

        Mockito.verify(mockedView).setBaseCurrency(modelEUR)
    }

    @Test
    fun `when landing on the screen, then show loading`() {
        // given

        Mockito.doReturn(modelEUR).`when`(mockedModelMapper)
            .mapToModel(domainEUR)
        // when

        presenter.init()

        // then

        Mockito.verify(mockedView).showLoading(true)
    }

    @Test
    fun `when landing on the screen and the interactor return Invalid list of currencies, then do show error`() =
        runBlocking {
            // given

            Mockito.doReturn(CurrenciesDomain.Invalid(ERROR_MESSAGE)).`when`(mockedInteractor)
                .getAllCurrencies(EUR, AMOUNT1.toFloat())
            // when

            presenter.init()

            // then

            Mockito.verify(mockedView).showError(ERROR_MESSAGE)
        }

    @Test
    fun `when landing on the screen and the interactor return Invalid list of currencies, then hide loading`() =
        runBlocking {
            // given

            Mockito.doReturn(CurrenciesDomain.Invalid(ERROR_MESSAGE)).`when`(mockedInteractor)
                .getAllCurrencies(EUR, AMOUNT1.toFloat())
            // when

            presenter.init()

            // then

            Mockito.verify(mockedView).showLoading(false)
        }

    @Test
    fun `when landing on the screen and the interactor return an empty list of currencies, then set the view to empty list`() =
        runBlocking {
            // given

            Mockito.doReturn(CurrenciesDomain.Valid(emptyList())).`when`(mockedInteractor)
                .getAllCurrencies(EUR, AMOUNT1.toFloat())
            // when

            presenter.init()

            // then

            Mockito.verify(mockedView).setItems(emptyList())
        }

    @Test
    fun `when landing on the screen and the interactor return an empty list of currencies, then hide loading`() =
        runBlocking {
            // given

            Mockito.doReturn(CurrenciesDomain.Valid(emptyList())).`when`(mockedInteractor)
                .getAllCurrencies(EUR, AMOUNT1.toFloat())
            // when

            presenter.init()

            // then

            Mockito.verify(mockedView).showLoading(false)
        }

    @Test
    fun `when landing on the screen and the interactor return a none empty list of currencies, then set the view with correct data`() =
        runBlocking {
            // given

            Mockito.doReturn(CurrenciesDomain.Valid(listOf(domainGBP))).`when`(mockedInteractor)
                .getAllCurrencies(EUR, AMOUNT1.toFloat())
            // when

            presenter.init()

            // then

            val expectedList = listOf(modelGBP)
            Mockito.verify(mockedView).setItems(expectedList)
        }

    @Test
    fun `when landing on the screen and the interactor return a none empty list of currencies, then hide loading`() =
        runBlocking {
            // given

            Mockito.doReturn(CurrenciesDomain.Valid(listOf(domainGBP))).`when`(mockedInteractor)
                .getAllCurrencies(EUR, AMOUNT1.toFloat())
            // when

            presenter.init()

            // then

            Mockito.verify(mockedView).showLoading(false)
        }

    @Test
    fun `when user click on a currency in the currencies list, then set this currency as the base currency`() =
        runBlocking {
            // given

            val newList = listOf(domainEUR)
            Mockito.doReturn(CurrenciesDomain.Valid(newList)).`when`(mockedInteractor)
                .getAllCurrencies(GBP, AMOUNT1.toFloat())
            // when

            presenter.onCurrencyClicked(modelGBP)

            // then

            Mockito.verify(mockedView).setBaseCurrency(modelGBP)
        }

    @Test
    fun `when user click on a currency in the currencies list, then show loading`() =
        runBlocking {
            // given

            // when

            presenter.onCurrencyClicked(modelGBP)

            // then

            Mockito.verify(mockedView).showLoading(true)
        }

    @Test
    fun `when user click on a currency in the currencies list and interactor return a valid list, then hide loading and show the new expected list`() =
        runBlocking {
            // given

            val initialList = listOf(domainGBP)
            Mockito.doReturn(CurrenciesDomain.Valid(initialList)).`when`(mockedInteractor)
                .getAllCurrencies(EUR, AMOUNT1.toFloat())
            val newList = listOf(domainEUR)
            Mockito.doReturn(CurrenciesDomain.Valid(newList)).`when`(mockedInteractor)
                .getAllCurrencies(GBP, AMOUNT1.toFloat())
            presenter.init()

            // when

            presenter.onCurrencyClicked(modelGBP)

            // then

            Mockito.verify(mockedView, times(2)).showLoading(false)
            val expectedList = listOf(modelEUR)
            Mockito.verify(mockedView).setItems(expectedList)
        }

    @Test
    fun `when user type any invalid number, i,e, not Float number, then set the same list with zero amount`() =
        runBlocking {
            // given

            val initialList = listOf(domainEUR, domainGBP)
            Mockito.doReturn(CurrenciesDomain.Valid(initialList)).`when`(mockedInteractor)
                .getAllCurrencies(EUR, AMOUNT1.toFloat())

            presenter.init()

            // when

            presenter.onTypingQuery("any none Float number, like symbol or . or string")

            // then

            val domainEUR0 = modelEUR.copy(amount = "0.0")
            val domainGBP0 = modelGBP.copy(amount = "0.0")
            val expectedList = listOf(domainEUR0, domainGBP0)
            Mockito.verify(mockedView).setItems(expectedList)
        }

    @Test
    fun `when user type a float number and interactor return an invalid response, then hide loading and show error`() =
        runBlocking {
            // given
            val newAmount = "5.6"
            Mockito.doReturn(CurrenciesDomain.Invalid(ERROR_MESSAGE)).`when`(mockedInteractor)
                .getAllCurrencies(EUR, newAmount.toFloat())
            // when

            presenter.onTypingQuery(newAmount)

            // then

            Mockito.verify(mockedView).showLoading(false)
            Mockito.verify(mockedView).showError(ERROR_MESSAGE)
        }

    @Test
    fun `when user type a float number and interactor return an empty list, then set the same list with zero amount`() =
        runBlocking {
            // given
            val newAmount = "5.6"
            val initialList = listOf(domainEUR, domainGBP)
            Mockito.doReturn(CurrenciesDomain.Valid(initialList)).`when`(mockedInteractor)
                .getAllCurrencies(EUR, AMOUNT1.toFloat())

            Mockito.doReturn(CurrenciesDomain.Valid(emptyList())).`when`(mockedInteractor)
                .getAllCurrencies(EUR, newAmount.toFloat())

            presenter.init()

            // when

            presenter.onTypingQuery(newAmount)

            // then

            val domainEUR0 = modelEUR.copy(amount = "0.0")
            val domainGBP0 = modelGBP.copy(amount = "0.0")
            val expectedList = listOf(domainEUR0, domainGBP0)
            Mockito.verify(mockedView).setItems(expectedList)
        }

    @Test
    fun `when user type a float number and interactor return a valid list of currencies, then set the correct result `() =
        runBlocking {
            // given
            val newAmount = "5.6"
            val initialList = listOf(domainEUR, domainGBP)
            Mockito.doReturn(CurrenciesDomain.Valid(initialList)).`when`(mockedInteractor)
                .getAllCurrencies(EUR, newAmount.toFloat())
            // when

            presenter.onTypingQuery(newAmount)

            // then

            val expectedList = listOf(modelEUR, modelGBP)
            Mockito.verify(mockedView).setItems(expectedList)
        }

    @Test
    fun `when user submit a search, then hide keyboard`() =
        runBlocking {
            // given

            // when

            presenter.submitSearch("any ")

            // then

            Mockito.verify(mockedView).hideKeyBoard()
        }

    @Test
    fun `when user submit a search for any invalid number, i,e, not Float number, then set the same list with zero amount`() =
        runBlocking {
            // given

            val initialList = listOf(domainEUR, domainGBP)
            Mockito.doReturn(CurrenciesDomain.Valid(initialList)).`when`(mockedInteractor)
                .getAllCurrencies(EUR, AMOUNT1.toFloat())

            presenter.init()

            // when

            presenter.submitSearch("any none Float number, like symbol or . or string")

            // then

            val domainEUR0 = modelEUR.copy(amount = "0.0")
            val domainGBP0 = modelGBP.copy(amount = "0.0")
            val expectedList = listOf(domainEUR0, domainGBP0)
            Mockito.verify(mockedView).setItems(expectedList)
        }

    @Test
    fun `when user submit a search  a float number, then show loading`() =
        runBlocking {
            // given
            val newAmount = "5.6"
            // when

            presenter.submitSearch(newAmount)

            // then

            Mockito.verify(mockedView).showLoading(true)
        }

    @Test
    fun `when user submit a search a float number and interactor return an invalid response, then hide loading and show error`() =
        runBlocking {
            // given
            val newAmount = "5.6"
            Mockito.doReturn(CurrenciesDomain.Invalid(ERROR_MESSAGE)).`when`(mockedInteractor)
                .getAllCurrencies(EUR, newAmount.toFloat())
            // when

            presenter.submitSearch(newAmount)

            // then

            Mockito.verify(mockedView).showLoading(false)
            Mockito.verify(mockedView).showError(ERROR_MESSAGE)
        }

    @Test
    fun `when user submit a search a float number and interactor return an empty list, then set the same list with zero amount`() =
        runBlocking {
            // given
            val newAmount = "5.6"
            val initialList = listOf(domainEUR, domainGBP)
            Mockito.doReturn(CurrenciesDomain.Valid(initialList)).`when`(mockedInteractor)
                .getAllCurrencies(EUR, AMOUNT1.toFloat())

            Mockito.doReturn(CurrenciesDomain.Valid(emptyList())).`when`(mockedInteractor)
                .getAllCurrencies(EUR, newAmount.toFloat())

            presenter.init()

            // when

            presenter.submitSearch(newAmount)

            // then

            val domainEUR0 = modelEUR.copy(amount = "0.0")
            val domainGBP0 = modelGBP.copy(amount = "0.0")
            val expectedList = listOf(domainEUR0, domainGBP0)
            Mockito.verify(mockedView).setItems(expectedList)
        }

    @Test
    fun `when user submit a search a float number and interactor return a valid list of currencies, then set the correct result `() =
        runBlocking {
            // given
            val newAmount = "5.6"
            val initialList = listOf(domainEUR, domainGBP)
            Mockito.doReturn(CurrenciesDomain.Valid(initialList)).`when`(mockedInteractor)
                .getAllCurrencies(EUR, newAmount.toFloat())
            // when

            presenter.submitSearch(newAmount)

            // then

            val expectedList = listOf(modelEUR, modelGBP)
            Mockito.verify(mockedView).setItems(expectedList)
        }

    companion object {
        private const val ERROR_MESSAGE = "Error message"
        private const val GENERIC_ERROR_MESSAGE = "Generic Error message"

        private const val EUR = "EUR"
        private const val EURO = "Euro"
        private const val SYMBOL_EUR = "€"
        private const val AMOUNT1 = "1.0"
        private const val FLAG_EUR = R.drawable.flag_eur

        private const val GBP = "GBP"
        private const val GREAT_BRITAIN_POUND = "British Pound"
        private const val SYMBOL_GBP = "£"
        private const val FLAG_GBP = R.drawable.flag_gbp

        private val domainEUR = CurrencyDomain(
            EUR,
            EURO,
            SYMBOL_EUR,
            AMOUNT1,
            FLAG_EUR
        )

        private val modelEUR = CurrencyModel(
            EUR,
            EURO,
            SYMBOL_EUR,
            AMOUNT1,
            FLAG_EUR
        )

        private val domainGBP = CurrencyDomain(
            GBP,
            GREAT_BRITAIN_POUND,
            SYMBOL_GBP,
            AMOUNT1,
            FLAG_GBP
        )

        private val modelGBP = CurrencyModel(
            GBP,
            GREAT_BRITAIN_POUND,
            SYMBOL_GBP,
            AMOUNT1,
            FLAG_GBP
        )
    }

}