package com.currenciesconverter.data

import com.google.gson.annotations.SerializedName

data class CurrenciesDTO(
    @SerializedName("base") val base: String,
    @SerializedName("date") val date: String,
    @SerializedName("rates") val rates: Map<String, String>
)