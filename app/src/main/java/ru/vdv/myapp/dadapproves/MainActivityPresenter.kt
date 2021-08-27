package ru.vdv.myapp.mygitapiapp

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import moxy.MvpView
import ru.vdv.myapp.dadapproves.presentation.interfaces.IScreens

class MainActivityPresenter(val router: Router, val screens: IScreens) : MvpPresenter<MvpView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(screens.main())
    }

    fun backClicked() {
        router.exit()
    }
}