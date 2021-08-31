package ru.vdv.myapp.dadapproves.presentation.contentview

import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.vdv.myapp.dadapproves.myschedulers.IMySchedulers
import ru.vdv.myapp.dadapproves.presentation.interfaces.ContentView

class ContentViewPresenter(
    private var moderationMode: Boolean? = false,
    private val category: Int? = 1,
    private val schedulers: IMySchedulers,
    private val router: Router
) : MvpPresenter<ContentView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.hideModeratorBtnGroup()
        viewState.clearStatus()
        viewState.setTag("Пиривет!")
        moderationMode?.let {
            if (it) {
                viewState.moderatorModeInit()
                viewState.showModeratorBtnGroup()
            }
        }
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    fun btnBackPressed() {
    }

    fun btnNextPressed() {
    }

    fun btnApprovePressed() {
    }

    fun btnForbidPressed() {
    }

    fun saveTag(s: String) {
    }
}