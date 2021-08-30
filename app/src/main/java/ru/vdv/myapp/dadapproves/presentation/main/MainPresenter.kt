package ru.vdv.myapp.dadapproves.presentation.main

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.CompositeDisposable
import moxy.MvpPresenter
import ru.vdv.myapp.dadapproves.myschedulers.IMySchedulers
import ru.vdv.myapp.dadapproves.presentation.interfaces.MainView
import ru.vdv.myapp.mygitapiapp.AndroidScreens

class MainPresenter(
    private val schedulers: IMySchedulers,
    private val router: Router
) : MvpPresenter<MainView>() {

    val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun goToDadLock() {
        router.navigateTo(AndroidScreens().dadLock())
    }

    fun goToContentView(i: Int) {
        router.navigateTo(AndroidScreens().contentView(1,i))
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}