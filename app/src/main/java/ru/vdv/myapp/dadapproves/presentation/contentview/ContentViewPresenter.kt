package ru.vdv.myapp.dadapproves.presentation.contentview

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.vdv.myapp.dadapproves.data.model.Joke
import ru.vdv.myapp.dadapproves.data.model.JokesRepository
import ru.vdv.myapp.dadapproves.data.model.NetworkState
import ru.vdv.myapp.dadapproves.data.model.RoomJoke
import ru.vdv.myapp.dadapproves.data.net.NetworkStateObservable
import ru.vdv.myapp.dadapproves.myschedulers.IMySchedulers
import ru.vdv.myapp.dadapproves.presentation.interfaces.ContentView

class ContentViewPresenter(
    private  val context: Context,
    private var moderationMode: Boolean = false,
    private val category: Int? = 1,
    private val jokesRepository: JokesRepository,
    private val schedulers: IMySchedulers,
    private val router: Router
) : MvpPresenter<ContentView>() {
    private var currentJoke: RoomJoke? = null
    private val disposables = CompositeDisposable()
//    private val connect = NetworkStateObservable(context)
//        .doOnNext{onNext(0, it)}
//        .publish()
//        .connect()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.hideModeratorBtnGroup()
        viewState.clearStatus()
        viewState.setTag("Пиривет!")
        if (moderationMode) {
            viewState.moderatorModeInit()
            viewState.showModeratorBtnGroup()
        }
        loadNextJokeFromStorage()
        disposables += NetworkStateObservable(context)
            .doOnNext{onNext(it)}
            .publish()
            .connect()
    }

    private fun onNext(networkState: NetworkState) {
        Toast.makeText(context, "Сеть: ${networkState.tag}", Toast.LENGTH_SHORT).show()
        when(networkState){
            NetworkState.DISCONNECTED -> viewState.disableBtnLoadNewJokeFromNetwork()
            NetworkState.CONNECTED -> viewState.enableBtnLoadNewJokeFromNetwork()
        }

    }

    private fun loadNextJokeFromStorage() {
        category?.let {
            (if (moderationMode) {
                Log.d(
                    "Моя проверка / презентер",
                    "Выполняю запрос очередной шутки в режиме модерации"
                )
                jokesRepository.getNextOne(it, currentJoke?.id ?: 0)
            } else {
                Log.d(
                    "Моя проверка / презентер",
                    "Выполняю запрос очередной шутки в режиме защищенного просмотра"
                )
                jokesRepository.getNextOneApproves(it, currentJoke?.id ?: 0)
            }).apply {
                observeOn(schedulers.main())
                    .subscribe(object : SingleObserver<RoomJoke> {
                        override fun onSubscribe(d: Disposable) {
                            disposables.add(d)
                        }

                        override fun onSuccess(t: RoomJoke) {
                            onLoadJokeFromStorageSuccess(t)
                        }

                        override fun onError(e: Throwable) {
                            onLoadNewJokeFromNetError(e)
                        }
                    })
            }
        }
    }

    private fun loadPreviousJokeFromStorage() {
        category?.let {
            (if (moderationMode) {
                Log.d(
                    "Моя проверка / презентер",
                    "Выполняю запрос очередной шутки в режиме модерации"
                )
                jokesRepository.getPreviousOne(it, currentJoke?.id ?: 0)
            } else {
                Log.d(
                    "Моя проверка / презентер",
                    "Выполняю запрос очередной шутки в режиме защищенного просмотра"
                )
                jokesRepository.getPreviousOneApproves(it, currentJoke?.id ?: 0)
            }).apply {
                observeOn(schedulers.main())
                    .subscribe(object : SingleObserver<RoomJoke> {
                        override fun onSubscribe(d: Disposable) {
                            disposables.add(d)
                        }

                        override fun onSuccess(t: RoomJoke) {
                            onLoadJokeFromStorageSuccess(t)
                        }

                        override fun onError(e: Throwable) {
                            onLoadNewJokeFromNetError(e)
                        }
                    })
            }
        }
    }

    private fun onLoadJokeFromStorageSuccess(t: RoomJoke) {
        Log.d("Моя проверка / презентер", "УСПЕХ. Репозиторий вернул из базы результат")
        Log.d("Моя проверка / презентер", "Объект имеет идентификатор: " + t.id)
        currentJoke = t
        // комплексная выдача результата на эеран
        displayingCurrentResult()
        //управление кнопками врепед и назад
        category?.let {
            (if (moderationMode) {
                Log.d(
                    "Моя проверка / презентер",
                    "Выполняю запрос очередной шутки в режиме модерации"
                )
                jokesRepository.getCountPrevious(it, currentJoke?.id ?: 0)
            } else {
                Log.d(
                    "Моя проверка / презентер",
                    "Выполняю запрос очередной шутки в режиме защищенного просмотра"
                )
                jokesRepository.getCountApprovesPrevious(it, currentJoke?.id ?: 0)
            }).apply {
                observeOn(schedulers.main())
                    .subscribe(object : SingleObserver<Int> {
                        override fun onSubscribe(d: Disposable) {
                            disposables.add(d)
                        }

                        override fun onSuccess(t: Int) {
                            Log.d(
                                "Моя проверка / презентер",
                                "Ответ получен! Количество шуток перед текущей: $t"
                            )
                            if (t > 0) viewState.showBtnBack()
                            else viewState.hideBtnBack()
                        }

                        override fun onError(e: Throwable) {
                            Log.d(
                                "Моя проверка / презентер",
                                "Получена ошибка при подсчете предыдущих шуток" +
                                        " в ответе репозитория: $e"
                            )
                            viewState.hideBtnBack()
                        }
                    })
            }
            (if (moderationMode) {
                Log.d(
                    "Моя проверка / презентер",
                    "Выполняю запрос очередной шутки в режиме модерации"
                )
                jokesRepository.getCountNext(it, currentJoke?.id ?: 0)
            } else {
                Log.d(
                    "Моя проверка / презентер",
                    "Выполняю запрос очередной шутки в режиме защищенного просмотра"
                )
                jokesRepository.getCountApprovesNext(it, currentJoke?.id ?: 0)
            }).apply {
                observeOn(schedulers.main())
                    .subscribe(object : SingleObserver<Int> {
                        override fun onSubscribe(d: Disposable) {
                            disposables.add(d)
                        }

                        override fun onSuccess(t: Int) {
                            Log.d(
                                "Моя проверка / презентер",
                                "Ответ получен! Количество шуток ПОСЛЕ текущей: $t"
                            )
                            if (t > 0) viewState.showBtnNext()
                            else viewState.hideBtnNext()
                        }

                        override fun onError(e: Throwable) {
                            Log.d(
                                "Моя проверка / презентер",
                                "Получена ошибка при подсчете следующих шуток" +
                                        " в ответе репозитория: $e"
                            )
                            viewState.hideBtnNext()
                        }
                    })
            }
        }
    }

    private fun displayingCurrentResult() {
        Log.d("Моя проверка / презентер", "Вывожу результат на экран")
        currentJoke?.let {
            viewState.setContent(it.content)
            viewState.setTag(it.labelTags)
            if (it.isApproved) {
                viewState.showStatusVerifiedAndApproved()
                viewState.disableBtnApprove()
                viewState.enableBtnForbidden()
            }
            if (it.isForbidden) {
                viewState.showStatusVerifiedAndForbidden()
                viewState.disableBtnForbidden()
                viewState.enableBtnApprove()
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
                        "---!!!!--- Загружен НЕуникальный контент, грузим по новой, старые данные не сохраняем"
                    )
                    loadNewJokeFromNet()
                } else {
                    viewState.setContent(t.content)
                    Log.d(
                        "Моя проверка / презентер",
                        "Порядок, контент уникален... Теперь создаю новый объект"
                    )
                    currentJoke = RoomJoke(
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
                    currentJoke?.let {
                        jokesRepository
                            .retainContent(it)
                            .observeOn(schedulers.main())
                            .subscribe(object : SingleObserver<Long> {
                                override fun onSubscribe(d: Disposable) {
                                    disposables.add(d)
                                }

                                override fun onSuccess(t: Long) {
                                    Log.d(
                                        "Моя проверка / презентер",
                                        "Новая запись добавлена с идентификатором $t"
                                    )
                                    it.id = t
                                }

                                override fun onError(e: Throwable) {
                                    onLoadNewJokeFromNetError(e)
                                }
                            })
                    }

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
        loadPreviousJokeFromStorage()
    }

    fun btnNextPressed() {
        loadNextJokeFromStorage()
    }

    fun btnApprovePressed() {
        currentJoke?.isViewedModerator = true
        currentJoke?.isApproved = true
        currentJoke?.isForbidden = false
        updateContent()
    }

    fun btnForbidPressed() {
        currentJoke?.isViewedModerator = true
        currentJoke?.isApproved = false
        currentJoke?.isForbidden = true
        updateContent()
    }

    fun saveTag(s: String) {
        currentJoke?.labelTags = s
        updateContent()
    }

    override fun onDestroy() {
        Log.d(
            "Моя проверка / презентер",
            "!!!!!   Сработал onDestroy")
        disposables.clear()
    }

    fun loadNewJokeFromNet() {
        category?.let {
            //disposables.clear()
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
        currentJoke?.let {
            jokesRepository
                .updateContent(it)
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
}