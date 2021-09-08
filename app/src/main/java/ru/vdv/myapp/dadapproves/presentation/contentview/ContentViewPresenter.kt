package ru.vdv.myapp.dadapproves.presentation.contentview

import android.util.Log
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.plusAssign
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
    private var currentRoomJoke = RoomJoke(
        "",
        0,
        category,
        "",
        isViewedModerator = false,
        isViewedUser = false,
        isApproved = false,
        isForbidden = false,
        estimation = 0
    )

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
                loadNewJokeFromNet()
            }
        }
    }

    private fun onLoadNewJokeFromNetError(e: Throwable) {
        Log.d("Моя проверка / презентер", "Получена ошибка в ответе репозитория: $e")
    }

    private fun onLoadNewJokeFromNetSuccess(t: Joke) {
        Log.d("Моя проверка / презентер", "УСПЕХ. Репозиторий вернул результат")
        Log.d("Моя проверка / презентер", "Объект содержит данные: " + t.content)
        //сначала проверяем пришедший результат на уникальность
        disposables += jokesRepository
            .getCountByContent(t.content)
            .observeOn(schedulers.main())
            .subscribe({ joke ->
                Log.d(
                    "Моя проверка / презентер",
                    "Проверка на уникальность выдала результат $joke"
                )
                if (joke > 0) {
                    Log.d(
                        "Моя проверка / презентер",
                        "---!!!!--- Загружен неуникальный контент, грузим по новой, старые данные не сохраняем"
                    )
                    loadNewJokeFromNet()
                } else {
                    viewState.setContent(t.content)
                    Log.d(
                        "Моя проверка / презентер",
                        "Порядок, контент уникален... Теперь создаю новый объект"
                    )
                    currentRoomJoke = RoomJoke(
                        t.content,
                        0,
                        category,
                        "",
                        isViewedModerator = false,
                        isViewedUser = false,
                        isApproved = false,
                        isForbidden = false,
                        estimation = 0
                    )
                    jokesRepository.retainContent(currentRoomJoke).observeOn(schedulers.main())
                        .subscribe(object : SingleObserver<Long> {
                            override fun onSubscribe(d: Disposable) {
                                disposables.add(d)
                            }

                            override fun onSuccess(t: Long) {
                                Log.d(
                                    "Моя проверка / презентер",
                                    "Новая запись добавлена с идентификатором $t"
                                )
                                currentRoomJoke.id = t
                            }

                            override fun onError(e: Throwable) {
                                onLoadNewJokeFromNetError(e)
                            }
                        })

                }
            },
                {
                    Log.d(
                        "Моя проверка / презентер",
                        "!!!!!   Проверка на уникальность выдала ошибку"
                    )
                }

            )

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
        currentRoomJoke.isViewedModerator = true
        currentRoomJoke.isApproved = true
        currentRoomJoke.isForbidden = false
        updateContent()
    }

    fun btnForbidPressed() {
        currentRoomJoke.isViewedModerator = true
        currentRoomJoke.isApproved = false
        currentRoomJoke.isForbidden = true
        updateContent()
    }

    fun saveTag(s: String) {
    }

    override fun onDestroy() {
        disposables.clear()
    }

    fun loadNewJokeFromNet() {
        category?.let {
            disposables.clear()
            jokesRepository
                .getContent(it)
                .observeOn(schedulers.main())
                .subscribe(object : SingleObserver<Joke> {
                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                    }

                    override fun onSuccess(t: Joke) {
                        onLoadNewJokeFromNetSuccess(t)
                    }

                    override fun onError(e: Throwable) {
                        onLoadNewJokeFromNetError(e)
                    }
                })
        }
    }

    private fun updateContent() {
        jokesRepository
            .updateContent(currentRoomJoke)
            .observeOn(schedulers.main())
            .subscribe(object : SingleObserver<Int> {
                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                }

                override fun onSuccess(t: Int) {
                    Log.d("Моя проверка", "Изменения сохранены для элемента $t")
                }

                override fun onError(e: Throwable) {
                    onLoadNewJokeFromNetError(e)
                }
            })

    }
}