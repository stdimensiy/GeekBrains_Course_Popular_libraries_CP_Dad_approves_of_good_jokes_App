package ru.vdv.myapp.dadapproves.presentation.contentview

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.vdv.myapp.dadapproves.myschedulers.IMySchedulers
import ru.vdv.myapp.dadapproves.presentation.interfaces.MainView

class ContentViewPresenter(
    private val schedulers: IMySchedulers,
    private val router: Router
) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }

}