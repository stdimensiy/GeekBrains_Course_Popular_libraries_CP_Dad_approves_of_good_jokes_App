package ru.vdv.myapp.dadapproves.presentation.contentview

import android.util.Log
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.vdv.myapp.dadapproves.data.model.Joke
import ru.vdv.myapp.dadapproves.data.model.JokesRepository
import ru.vdv.myapp.dadapproves.data.model.RoomJoke
import ru.vdv.myapp.dadapproves.myschedulers.IMySchedulers
import ru.vdv.myapp.dadapproves.presentation.interfaces.ContentView

class ContentViewPresenter(
    private var moderationMode: Boolean? = false,
    private val category: Int? = 1,
    private val jokesRepository: JokesRepository,
    private val schedulers: IMySchedulers,
    private val router: Router
) : MvpPresenter<ContentView>() {
    private val disposables = CompositeDisposable()

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
                category?.let {
                    jokesRepository
                        .getContent(it)
                        .observeOn(schedulers.main())
                        .subscribe(object : SingleObserver<Joke> {
                            override fun onSubscribe(d: Disposable) {
                                disposables.add(d)
                            }

                            override fun onSuccess(t: Joke) {
                                onGetContentSuccess(t)
                            }

                            override fun onError(e: Throwable) {
                                onGetContentError(e)
                            }
                        })
                }
            }
        }
    }

    private fun onGetContentError(e: Throwable) {
        Log.d("Моя проверка / презентер", "Получена ошибка в ответе репозитория")
    }

    private fun onGetContentSuccess(t: Joke) {
        Log.d("Моя проверка / презентер", "УСПЕХ. Репозиторий вернул результат")
        Log.d("Моя проверка / презентер", "Объект содержит данные: " + t.content)
        viewState.setContent(t.content)
        Log.d("Моя проверка / презентер", "Теперь создаю новый объект")
        val currentRoomJoke = RoomJoke(
            t.content,
            0,
            category,
            "",
            false,
            false,
            false,
            false,
            0
        )
        jokesRepository.retainContent(currentRoomJoke).observeOn(schedulers.main()).subscribe()

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

    override fun onDestroy() {
        disposables.clear()
    }
}