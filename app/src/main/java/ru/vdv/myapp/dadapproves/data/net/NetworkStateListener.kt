package ru.vdv.myapp.dadapproves.data.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import io.reactivex.rxjava3.android.MainThreadDisposable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import ru.vdv.myapp.dadapproves.data.model.NetworkState

class NetworkStateListener (
    private val context: Context,
    private val observer: Observer<in NetworkState>
) :   ConnectivityManager.NetworkCallback(), Disposable {

    private val connectivityManager: ConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    private val disposable = object : MainThreadDisposable() {
        override fun onDispose() {
            connectivityManager.unregisterNetworkCallback(this@NetworkStateListener)
        }
    }
    init {
        connectivityManager
            .registerNetworkCallback(NetworkRequest.Builder().build(),this)
    }

    override fun onAvailable(network: Network) {
        observer.onNext(NetworkState.CONNECTED)
    }

    override fun onLost(network: Network) {
        observer.onNext(NetworkState.DISCONNECTED)
    }

    override fun onUnavailable() {
        observer.onNext(NetworkState.DISCONNECTED)
    }

    override fun dispose() {
        Log.d("Моя проверка / NetworkStateListener", "Сработал dispose")
        disposable.dispose()
    }

    override fun isDisposed(): Boolean {
        return disposable.isDisposed
    }

}