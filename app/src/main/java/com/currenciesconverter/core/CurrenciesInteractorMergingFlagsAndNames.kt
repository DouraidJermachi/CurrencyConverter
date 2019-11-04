package com.currenciesconverter.core

import com.currenciesconverter.R

internal class CurrenciesInteractorMergingFlagsAndNames(
    private val interactor: CurrenciesContract.Interactor
) : CurrenciesContract.Interactor by interactor {
    override suspend fun getAllCurrencies(base: String, amount: Float?): CurrenciesDomain {
        val serverCurrencies = interactor.getAllCurrencies(base, amount)
        if (serverCurrencies is CurrenciesDomain.Valid) {
            val currencies = mutableListOf<CurrencyDomain>()
            serverCurrencies.currencies.map { serverCurrency ->
                flagsAndNamesCurrencies.map { localCurrency ->
                    if (serverCurrency.code == localCurrency.code) {
                        currencies.add(serverCurrency.merge(localCurrency))
                    }
                }
            }
            return CurrenciesDomain.Valid(currencies)
        }
        return serverCurrencies
    }

    private fun CurrencyDomain.merge(localCurrency: CurrencyDomain): CurrencyDomain =
        CurrencyDomain(
            localCurrency.code,
            localCurrency.name,
            localCurrency.symbol,
            amount,
            localCurrency.logo
        )

    companion object {
        val flagsAndNamesCurrencies = listOf(
            CurrencyDomain("EUR", "Euro", "€", null, R.drawable.flag_eur),
            CurrencyDomain("USD", "United States Dollar", "$", null, R.drawable.flag_usd),
            CurrencyDomain("GBP", "British Pound", "£", null, R.drawable.flag_gbp),
            CurrencyDomain("CZK", "Czech Koruna", "Kč", null, R.drawable.flag_czk),
            CurrencyDomain("TRY", "Turkish Lira", "₺", null, R.drawable.flag_try),
            CurrencyDomain("AED", "Emirati Dirham", "د.إ", null, R.drawable.flag_aed),
            CurrencyDomain("AFN", "Afghanistan Afghani", "؋", null, R.drawable.flag_afn),
            CurrencyDomain("ARS", "Argentine Peso", "$", null, R.drawable.flag_ars),
            CurrencyDomain("AUD", "Australian Dollar", "$", null, R.drawable.flag_aud),
            CurrencyDomain("BBD", "Barbados Dollar", "$", null, R.drawable.flag_bbd),
            CurrencyDomain("BDT", "Bangladeshi Taka", " Tk", null, R.drawable.flag_bdt),
            CurrencyDomain("BGN", "Bulgarian Lev", "лв", null, R.drawable.flag_bgn),
            CurrencyDomain("BHD", "Bahraini Dinar", "BD", null, R.drawable.flag_bhd),
            CurrencyDomain("BMD", "Bermuda Dollar", "$", null, R.drawable.flag_bmd),
            CurrencyDomain("BND", "Brunei Darussalam Dollar", "$", null, R.drawable.flag_bnd),
            CurrencyDomain("BOB", "Bolivia Bolíviano", "$" + "b", null, R.drawable.flag_bob),
            CurrencyDomain("BRL", "Brazil Real", "R$", null, R.drawable.flag_brl),
            CurrencyDomain("BTN", "Bhutanese Ngultrum", "Nu.", null, R.drawable.flag_btn),
            CurrencyDomain("BZD", "Belize Dollar", "BZ$", null, R.drawable.flag_bzd),
            CurrencyDomain("CAD", "Canada Dollar", "$", null, R.drawable.flag_cad),
            CurrencyDomain("CHF", "Switzerland Franc", "CHF", null, R.drawable.flag_chf),
            CurrencyDomain("CLP", "Chile Peso", "$", null, R.drawable.flag_clp),
            CurrencyDomain("CNY", "China Yuan Renminbi", "¥", null, R.drawable.flag_cny),
            CurrencyDomain("COP", "Colombia Peso", "$", null, R.drawable.flag_cop),
            CurrencyDomain("CRC", "Costa Rica Colon", "₡", null, R.drawable.flag_crc),
            CurrencyDomain("DKK", "Denmark Krone", "kr", null, R.drawable.flag_dkk),
            CurrencyDomain("DOP", "Dominican Republic Peso", "RD$", null, R.drawable.flag_dop),
            CurrencyDomain("EGP", "Egypt Pound", "£", null, R.drawable.flag_egp),
            CurrencyDomain("ETB", "Ethiopian Birr", "Br", null, R.drawable.flag_etb),
            CurrencyDomain("GEL", "Georgian Lari", "₾", null, R.drawable.flag_gel),
            CurrencyDomain("GHS", "Ghana Cedi", "¢", null, R.drawable.flag_ghs),
            CurrencyDomain("GMD", "Gambian dalasi", "D", null, R.drawable.flag_gmd),
            CurrencyDomain("GYD", "Guyana Dollar", "$", null, R.drawable.flag_gyd),
            CurrencyDomain("HKD", "Hong Kong Dollar", "$", null, R.drawable.flag_hkd),
            CurrencyDomain("HRK", "Croatia Kuna", "kn", null, R.drawable.flag_hrk),
            CurrencyDomain("HUF", "Hungary Forint", "Ft", null, R.drawable.flag_huf),
            CurrencyDomain("IDR", "Indonesia Rupiah", "Rp", null, R.drawable.flag_idr),
            CurrencyDomain("ILS", "Israel Shekel", "₪", null, R.drawable.flag_ils),
            CurrencyDomain("INR", "Indian Rupee", "₹", null, R.drawable.flag_inr),
            CurrencyDomain("ISK", "Iceland Krona", "kr", null, R.drawable.flag_isk),
            CurrencyDomain("JMD", "Jamaica Dollar", "J$", null, R.drawable.flag_jmd),
            CurrencyDomain("JPY", "Japanese Yen", "¥", null, R.drawable.flag_jpy),
            CurrencyDomain("KES", "Kenyan Shilling", "KSh", null, R.drawable.flag_kes),
            CurrencyDomain("KRW", "Korea (South) Won", "₩", null, R.drawable.flag_krw),
            CurrencyDomain("KWD", "Kuwaiti Dinar", "د.ك", null, R.drawable.flag_kwd),
            CurrencyDomain("KYD", "Cayman Islands Dollar", "$", null, R.drawable.flag_kyd),
            CurrencyDomain("KZT", "Kazakhstan Tenge", "лв", null, R.drawable.flag_kzt),
            CurrencyDomain("LAK", "Laos Kip", "₭", null, R.drawable.flag_lak),
            CurrencyDomain("LKR", "Sri Lanka Rupee", "₨", null, R.drawable.flag_lkr),
            CurrencyDomain("LRD", "Liberia Dollar", "$", null, R.drawable.flag_lrd),
            CurrencyDomain("LTL", "Lithuanian Litas", "Lt", null, R.drawable.flag_ltl),
            CurrencyDomain("MAD", "Moroccan Dirham", "MAD", null, R.drawable.flag_mad),
            CurrencyDomain("MDL", "Moldovan Leu", "MDL", null, R.drawable.flag_mdl),
            CurrencyDomain("MKD", "Macedonia Denar", "ден", null, R.drawable.flag_mkd),
            CurrencyDomain("MNT", "Mongolia Tughrik", "₮", null, R.drawable.flag_mnt),
            CurrencyDomain("MUR", "Mauritius Rupee", "₨", null, R.drawable.flag_mur),
            CurrencyDomain("MWK", "Malawian Kwacha", "MK", null, R.drawable.flag_mwk),
            CurrencyDomain("MXN", "Mexico Peso", "$", null, R.drawable.flag_mxn),
            CurrencyDomain("MYR", "Malaysia Ringgit", "RM", null, R.drawable.flag_myr),
            CurrencyDomain("MZN", "Mozambique Metical", "MT", null, R.drawable.flag_mzn),
            CurrencyDomain("NAD", "Namibia Dollar", "$", null, R.drawable.flag_nad),
            CurrencyDomain("NGN", "Nigeria Naira", "₦", null, R.drawable.flag_ngn),
            CurrencyDomain("NIO", "Nicaragua Cordoba", "C$", null, R.drawable.flag_nio),
            CurrencyDomain("NOK", "Norway Krone", "kr", null, R.drawable.flag_nok),
            CurrencyDomain("NPR", "Nepal Rupee", "₨", null, R.drawable.flag_npr),
            CurrencyDomain("NZD", "New Zealand Dollar", "$", null, R.drawable.flag_nzd),
            CurrencyDomain("OMR", "Oman Rial", "﷼", null, R.drawable.flag_omr),
            CurrencyDomain("PEN", "Peru Sol", "S/.", null, R.drawable.flag_pen),
            CurrencyDomain("PGK", "Papua New Guinean Kina", "K", null, R.drawable.flag_pgk),
            CurrencyDomain("PHP", "Philippines Peso", "₱", null, R.drawable.flag_php),
            CurrencyDomain("PKR", "Pakistan Rupee", "₨", null, R.drawable.flag_pkr),
            CurrencyDomain("PLN", "Poland Zloty", "zł", null, R.drawable.flag_pln),
            CurrencyDomain("PYG", "Paraguay Guarani", "Gs", null, R.drawable.flag_pyg),
            CurrencyDomain("QAR", "Qatar Riyal", "﷼", null, R.drawable.flag_qar),
            CurrencyDomain("RON", "Romania Leu", "lei", null, R.drawable.flag_ron),
            CurrencyDomain("RSD", "Serbia Dinar", "Дин.", null, R.drawable.flag_rsd),
            CurrencyDomain("RUB", "Russia Ruble", "₽", null, R.drawable.flag_rub),
            CurrencyDomain("SAR", "Saudi Arabia Riyal", "﷼", null, R.drawable.flag_sar),
            CurrencyDomain("SEK", "Sweden Krona", "kr", null, R.drawable.flag_sek),
            CurrencyDomain("SGD", "Singapore Dollar", "$", null, R.drawable.flag_sgd),
            CurrencyDomain("SOS", "Somalia Shilling", "S", null, R.drawable.flag_sos),
            CurrencyDomain("SRD", "Suriname Dollar", "$", null, R.drawable.flag_srd),
            CurrencyDomain("THB", "Thailand Baht", "฿", null, R.drawable.flag_thb),
            CurrencyDomain("TTD", "Trinidad and Tobago Dollar", "TT$", null, R.drawable.flag_ttd),
            CurrencyDomain("TWD", "Taiwan New Dollar", "NT$", null, R.drawable.flag_twd),
            CurrencyDomain("TZS", "Tanzanian Shilling", "TSh", null, R.drawable.flag_tzs),
            CurrencyDomain("UAH", "Ukraine Hryvnia", "₴", null, R.drawable.flag_uah),
            CurrencyDomain("UGX", "Ugandan Shilling", "USh", null, R.drawable.flag_ugx),
            CurrencyDomain("UYU", "Uruguay Peso", "$" + "U", null, R.drawable.flag_uyu),
            CurrencyDomain("VEF", "Venezuela Bolívar", "Bs", null, R.drawable.flag_vef),
            CurrencyDomain("VND", "Viet Nam Dong", "₫", null, R.drawable.flag_vnd),
            CurrencyDomain("YER", "Yemen Rial", "﷼", null, R.drawable.flag_yer),
            CurrencyDomain("ZAR", "South Africa Rand", "R", null, R.drawable.flag_zar)
        )
    }
}

