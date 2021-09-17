package ru.vdv.myapp.dadapproves.data.net

import android.content.Context
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import ru.vdv.myapp.dadapproves.data.model.NetworkState


class NetworkStateObservable(private val context: Context) : Observable<NetworkState>() {
    override fun subscribeActual(observer: Observer<in NetworkState>) {
        val listener = NetworkStateListener(context, observer)
        observer.onSubscribe(listener)
    }
}