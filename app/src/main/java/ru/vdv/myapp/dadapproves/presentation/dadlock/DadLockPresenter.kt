package ru.vdv.myapp.dadapproves.presentation.dadlock

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.vdv.myapp.dadapproves.AndroidScreens
import ru.vdv.myapp.dadapproves.myschedulers.IMySchedulers
import ru.vdv.myapp.dadapproves.presentation.interfaces.AuthView

class DadLockPresenter(
    private val router: Router
) : MvpPresenter<AuthView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.hideErrorMessage()
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    fun checkUser(s: String) {
        if (checkUserPassword(s)) {
            viewState.hideErrorMessage()
            router.navigateTo(AndroidScreens().dadsOffice())
        } else {
            viewState.showErrorMessage()
            viewState.clearPasswordView()
        }
    }

    private fun checkUserPassword(password: String): Boolean {
        if (password == "123") return true // на данном этапе так для тестирования
        return false
    }
}