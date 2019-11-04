package com.currenciesconverter.utils

import android.os.AsyncTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Unconfined
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import kotlin.coroutines.CoroutineContext

interface CoroutineDispatcherFactory {

    val IODispatcher: CoroutineContext

    val UIDispatcher: CoroutineContext

}

class CoroutineDispatcherFactoryDefault : CoroutineDispatcherFactory {
    override val IODispatcher: CoroutineContext
        get() = AsyncTask.THREAD_POOL_EXECUTOR.asCoroutineDispatcher()

    override val UIDispatcher: CoroutineContext
        get() = Dispatchers.Main

}

@ExperimentalCoroutinesApi
class CoroutineDispatcherFactoryUnconfined : CoroutineDispatcherFactory {

    override val IODispatcher: CoroutineContext
        get() = Unconfined

    override val UIDispatcher: CoroutineContext
        get() = Unconfined
}