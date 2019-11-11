# CurrencyConverter
 * This project intreduces an usecase of the Kotlin "channel"
 * This app is getting the rates for different currencies from an API, while writing the amount in an EditText
 * The channel makes sure that the data comming from the API is always correct for the query amount on the EditText while typing, by queuing the API requests and result through the channel (the last response is always for the last request).
 * The app update the result every 1 sec
 * It's fully tested (unit tested)
