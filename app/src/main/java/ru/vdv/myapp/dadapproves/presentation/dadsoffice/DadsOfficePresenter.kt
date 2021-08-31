package ru.vdv.myapp.dadapproves.presentation.dadsoffice

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.vdv.myapp.dadapproves.AndroidScreens
import ru.vdv.myapp.dadapproves.myschedulers.IMySchedulers
import ru.vdv.myapp.dadapproves.presentation.interfaces.MainView

class DadsOfficePresenter(
    private val schedulers: IMySchedulers,
    private val router: Router
) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun goToContentView(i: Int) {
        router.navigateTo(AndroidScreens().contentView(true, i))
    }

    fun backPressed(): Boolean {
        router.backTo(AndroidScreens().main())
        return true
    }
}