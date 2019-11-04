package com.currenciesconverter.app

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.currenciesconverter.R
import com.currenciesconverter.core.CurrenciesContract
import com.currenciesconverter.core.CurrencyModel
import com.currenciesconverter.utils.NewAndroidUtils
import com.currenciesconverter.utils.SnackbarDecorator
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_currencies.*


internal class CurrenciesFragment :
    Fragment(),
    CurrenciesContract.View {

    companion object {

        fun newInstance() = CurrenciesFragment()
    }

    private lateinit var presenter: CurrenciesContract.Presenter
    private lateinit var newAndroidUtils: NewAndroidUtils
    private lateinit var adapter: CurrenciesAdapter
    private lateinit var textWatcher: TextWatcher

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val serviceLocator = CurrenciesServiceLocator(context)
        presenter = serviceLocator.getPresenter()
        adapter = serviceLocator.getCurrenciesAdabter(presenter)
        newAndroidUtils = serviceLocator.getNewAndroidUtils()
        textWatcher = serviceLocator.getTextWatcher(presenter)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currencies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.bindView(this)

        recyclerView?.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView?.overScrollMode = View.OVER_SCROLL_NEVER
        recyclerView?.setHasFixedSize(true)


        queryEditText?.addTextChangedListener(textWatcher)
        queryEditText?.setOnEditorActionListener { textView, actionId, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                presenter.submitSearch(textView.text)
                true
            } else {
                false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.init()
    }

    override fun onDestroyView() {
        presenter.unbindView()
        super.onDestroyView()
    }

    override fun showLoading(visible: Boolean) {
        progressBar.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun showError(errorMessage: String) {
        withContext {
            showDefaultError(it, errorMessage)
        }
    }

    override fun setBaseCurrency(queryCurrency: CurrencyModel) {
        currencyCode.text = queryCurrency.code
        currencyName.text =
            getString(R.string.currency_name_plus_symbol, queryCurrency.name, queryCurrency.symbol)
        queryEditText.setText(queryCurrency.amount)

        Glide.with(this)
            .load(queryCurrency.logo)
            .apply(RequestOptions().circleCrop())
            .into(avatarImageView)
    }

    override fun setItems(items: List<CurrencyModel>) {
        adapter.setItems(items)
        adapter.notifyDataSetChanged()

    }

    override fun hideKeyBoard() {
        view?.let {
            newAndroidUtils.requestHideKeyboard(it)
        }
    }

    private fun showDefaultError(
        context: Context,
        errorMessage: CharSequence,
        optimalViewForSnackbar: View? = view
    ) {
        if (optimalViewForSnackbar == null || errorMessage.length >= 80) {
            AlertDialog.Builder(context)
                .setMessage(errorMessage)
                .create()
                .show()
        } else {
            val snackbar = Snackbar.make(optimalViewForSnackbar, errorMessage, Snackbar.LENGTH_LONG)
            SnackbarDecorator.decorate(snackbar)
            snackbar.show()
        }
    }

}

private inline fun Fragment.withContext(block: (Context) -> Unit) {
    context?.let { block(it) }
}