package ru.vdv.myapp.dadapproves.presentation.contentview

import android.util.Log
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import ru.vdv.myapp.dadapproves.myschedulers.IMySchedulers
import ru.vdv.myapp.dadapproves.presentation.interfaces.ContentView

class ContentViewPresenter(
    private var moderationMode: Boolean = false,
    private val category: Int,
    private val schedulers: IMySchedulers,
    private val router: Router
) : MvpPresenter<ContentView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.hideModeratorBtnGroup()
        viewState.clearStatus()
        viewState.setTag("Пиривет!")
        Log.d("Моя проверка", "передан режим $moderationMode")
        if (moderationMode) {
            Log.d("Моя проверка", "Пытаюсь организовать админский режим")
            viewState.moderatorModeInit()
            viewState.showModeratorBtnGroup()
        }
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    fun btnBackPressed() {
        Log.d("Моя проверка", "Нажата кнопка управления контентом НАЗАД")
    }

    fun btnNextPressed() {
        Log.d("Моя проверка", "Нажата кнопка управления контентом ВПЕРЕД")
    }

    fun btnApprovePressed() {
        Log.d("Моя проверка", "Нажата кнопка одобрения контента")
    }

    fun btnForbidPressed() {
        Log.d("Моя проверка", "Нажата кнопка запрета контента")
    }

    fun saveTag(s: String) {
        Log.d("Моя проверка", "Попытка сохранить изменения в теге контента")
    }
}