package com.currenciesconverter.utils

import android.graphics.Color
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

object SnackbarDecorator {

    fun decorate(snackbar: Snackbar) {
        snackbar.setActionTextColor(Color.YELLOW)
        val snackbarView = snackbar.view
        try {
            val textView =
                snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView?.setTextColor(Color.RED)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        snackbarView.setBackgroundColor(Color.BLACK)
    }

}
