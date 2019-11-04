package com.currenciesconverter.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.currenciesconverter.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, CurrenciesFragment.newInstance())
                .commit()
        }
    }
}
