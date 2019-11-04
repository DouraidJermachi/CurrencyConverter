package com.currenciesconverter.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class NewAndroidUtils(private val mContext: Context) {

    fun requestHideKeyboard(view: View) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
