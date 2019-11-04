package com.currenciesconverter.core

import com.currenciesconverter.R
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class CurrenciesInteractorErrorHandlingTest {

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var mockedBaseInteractor: CurrenciesContract.Interactor

    private lateinit var interactor: CurrenciesContract.Interactor

    @Before
    fun setUp() {
        interactor = CurrenciesInteractorErrorHandling(
            mockedBaseInteractor
        )
    }

    @Test
    fun `when base interactor return a valid response, then do return the same valid response`() =
        runBlocking {
            // given

            val expectedResult = CurrenciesDomain.Valid(listOf(domainEUR, domainGBP))
            Mockito.doReturn(expectedResult).`when`(mockedBaseInteractor)
                .getAllCurrencies(USD, AMOUNT1.toFloat())
            // when

            val actualResult = interactor.getAllCurrencies(USD, AMOUNT1.toFloat())

            // then

            assertEquals(expectedResult, actualResult)
        }

    @Test
    fun `when base interactor return a valid empty list, then do return the same valid response`() =
        runBlocking {
            // given

            val expectedResult = CurrenciesDomain.Valid(emptyList())
            Mockito.doReturn(expectedResult).`when`(mockedBaseInteractor)
                .getAllCurrencies(USD, AMOUNT1.toFloat())
            // when

            val actualResult = interactor.getAllCurrencies(USD, AMOUNT1.toFloat())

            // then

            assertEquals(expectedResult, actualResult)
        }

    @Test
    fun `when base interactor throw Exception, then do return invalid data`() =
        runBlocking {
            // given

            val fakeException = FakeException()

            Mockito.doThrow(fakeException).`when`(mockedBaseInteractor)
                .getAllCurrencies(USD, AMOUNT1.toFloat())

            // when

            val actualResult = interactor.getAllCurrencies(USD, AMOUNT1.toFloat())

            // then

            assert(actualResult is CurrenciesDomain.Invalid)
            assertEquals((actualResult as CurrenciesDomain.Invalid).errorMessag, ERROR_MESSAGE)
        }


    companion object {
        private const val ERROR_MESSAGE = "fake exception"
        private const val USD = "USD"

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

        private val domainGBP = CurrencyDomain(
            GBP,
            GREAT_BRITAIN_POUND,
            SYMBOL_GBP,
            AMOUNT1,
            FLAG_GBP
        )

        private class FakeException : RuntimeException(ERROR_MESSAGE)
    }
}