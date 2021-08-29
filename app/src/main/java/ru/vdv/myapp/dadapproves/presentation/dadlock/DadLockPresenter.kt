package ru.vdv.myapp.dadapproves.presentation.dadlock

import android.util.Log
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.vdv.myapp.dadapproves.myschedulers.IMySchedulers
import ru.vdv.myapp.dadapproves.presentation.interfaces.AuthView
import ru.vdv.myapp.dadapproves.presentation.interfaces.MainView
import ru.vdv.myapp.mygitapiapp.AndroidScreens

class DadLockPresenter(
    private val schedulers: IMySchedulers,
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
        Log.d("Моя проверка", "Передан параметр $s")
        if (checkUserPassword(s)){
            viewState.hideErrorMessage()
            router.navigateTo(AndroidScreens().dadsOffice())
        }
        else {
            viewState.showErrorMessage()
            viewState.clearPasswordView()
        }
    }

    private fun checkUserPassword(password: String) : Boolean{
        if (password == "123") return true // на данном этапе так для тестирования
        return false
    }
}