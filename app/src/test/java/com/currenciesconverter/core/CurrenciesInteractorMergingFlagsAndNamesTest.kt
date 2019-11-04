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

class CurrenciesInteractorMergingFlagsAndNamesTest {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var mockedBaseInteractor: CurrenciesContract.Interactor

    private lateinit var interactor: CurrenciesContract.Interactor

    @Before
    fun setUp() {
        interactor = CurrenciesInteractorMergingFlagsAndNames(
            mockedBaseInteractor
        )
    }

    @Test
    fun `when base interactor return a valid list existed in the known list, then do return the same valid list filled with correct missing data`() =
        runBlocking {
            // given
            val networkDomainEUR = domainEUR.copy(name = null, symbol = null, logo = 0)
            val networkDomainGBP = domainGBP.copy(name = null, symbol = null, logo = 0)
            val networkCurrenciesList = listOf(networkDomainEUR, networkDomainGBP) // incomplete data

            Mockito.doReturn(CurrenciesDomain.Valid(networkCurrenciesList)).`when`(mockedBaseInteractor)
                .getAllCurrencies(USD, AMOUNT1.toFloat())
            // when

            val actualResult = interactor.getAllCurrencies(USD, AMOUNT1.toFloat())

            // then

            val expectedResult = CurrenciesDomain.Valid(listOf(domainEUR, domainGBP))
            assert(actualResult is CurrenciesDomain.Valid)
            assertEquals(expectedResult.currencies, (actualResult as CurrenciesDomain.Valid).currencies)
        }
    companion object {
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
    }
}