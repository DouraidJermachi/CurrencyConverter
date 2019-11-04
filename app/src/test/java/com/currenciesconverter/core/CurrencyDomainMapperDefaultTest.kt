package com.currenciesconverter.core

import com.currenciesconverter.data.CurrenciesDTO
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class CurrencyDomainMapperDefaultTest {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var mapper: CurrencyDomainMapper

    @Before
    fun setUp() {
        mapper = CurrencyDomainMapperDefault()
    }

    @Test
    fun `when using the mapper get the correct currencies list`() {
        // given

        // when

        val actualResult = mapper.map(responseDTO)
        // then

        val expectedResult = listOf(domainEUR, domainGBP)
        assertEquals(expectedResult, actualResult)
    }

    companion object {
        private const val USD = "USD"

        private const val EUR = "EUR"
        private const val AMOUNT1 = "1.0"

        private const val GBP = "GBP"

        private val domainEUR = CurrencyDomain(
            EUR,
            null,
            null,
            AMOUNT1,
            -1
        )

        private val domainGBP = CurrencyDomain(
            GBP,
            null,
            null,
            AMOUNT1,
            -1
        )

        private val responseDTO = CurrenciesDTO(
            USD,
            AMOUNT1,
            mapOf(Pair(EUR, AMOUNT1), Pair(GBP, AMOUNT1))
        )
    }
}