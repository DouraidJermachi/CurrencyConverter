package com.currenciesconverter.utils

import android.content.Context

import androidx.annotation.StringRes

interface StringResourceWrapper {
    fun getString(@StringRes stringResId: Int): String

    fun getString(@StringRes stringResId: Int, vararg formatArgs: Any): String
}

class StringResourceWrapperDefault(private val context: Context) : StringResourceWrapper {


    override fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }

    override fun getString(@StringRes stringResId: Int, vararg formatArgs: Any): String {
        return context.getString(stringResId, *formatArgs)
    }
}
