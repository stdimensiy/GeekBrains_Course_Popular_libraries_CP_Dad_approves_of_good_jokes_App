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
    private var currentJoke: RoomJoke? = null
    private var previousJokeId: Long = 0
    private var currentJokeId: Long = 0
    private var nextJokeId: Long = 0
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
            } else {
                loadNextJokeFromStorage()
            }
        }
    }

    private fun loadNextJokeFromStorage() {
        category?.let {
            jokesRepository
                .getNextOne(it, currentJoke?.id ?: 0)
                .observeOn(schedulers.main())
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

    private fun loadPreviousJokeFromStorage() {
        category?.let {
            jokesRepository
                .getPreviousOne(it, currentJoke?.id ?: 0)
                .observeOn(schedulers.main())
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

    private fun onLoadJokeFromStorageSuccess(t: RoomJoke) {
        Log.d("Моя проверка / презентер", "УСПЕХ. Репозиторий вернул из базы результат")
        Log.d("Моя проверка / презентер", "Объект имеет идентификатор: " + t.id)
        currentJoke = t
        // комплексная выдача результата на эеран
        displayingCurrentResult()
        //управление кнопками врепед и назад
        category?.let {
            jokesRepository
                .getCountPrevious(it, currentJoke?.id ?: 0)
                .observeOn(schedulers.main())
                .subscribe(object : SingleObserver<Int> {
                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                    }

                    override fun onSuccess(t: Int) {
                        Log.d("Моя проверка / презентер", "Ответ получен! Количество шуток перед текущей: $t")
                        if (t>0) viewState.showBtnBack()
                        else viewState.hideBtnBack()
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Моя проверка / презентер", "Получена ошибка при подсчете предыдущих шуток" +
                                " в ответе репозитория: $e")
                        viewState.hideBtnBack()
                    }
                })
            jokesRepository
                .getCountNext(it, currentJoke?.id ?: 0)
                .observeOn(schedulers.main())
                .subscribe(object : SingleObserver<Int> {
                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                    }

                    override fun onSuccess(t: Int) {
                        Log.d("Моя проверка / презентер", "Ответ получен! Количество шуток ПОСЛЕ текущей: $t")
                        if (t>0) viewState.showBtnNext()
                        else viewState.hideBtnNext()
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Моя проверка / презентер", "Получена ошибка при подсчете следующих шуток" +
                                " в ответе репозитория: $e")
                        viewState.hideBtnNext()
                    }
                })
        }


    }

    private fun displayingCurrentResult() {
        Log.d("Моя проверка / презентер", "Вывожу результат на экран")
        viewState.setContent(currentJoke?.content ?: "Пусто")
        viewState.setTag(currentJoke?.labelTags ?: "")
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
        loadPreviousJokeFromStorage()

//        //нажата кнопка петехода к предыдущей шутке.
//        loadJokesById(previousJokeId)
//        //currentJokeId = previousJokeId
//        loadJokeUpToId(currentJokeId)
    }

    private fun loadJokeUpToId(jokeId: Long) {
        category?.let {
            jokesRepository
                .getContentUpToId(jokeId, it)
                .observeOn(schedulers.main())
                .subscribe(object : SingleObserver<List<RoomJoke>> {
                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                    }

                    override fun onSuccess(t: List<RoomJoke>) {
                        onLoadPreviousJokeFromStorageSuccess(t)
                    }

                    override fun onError(e: Throwable) {
                        onLoadNewJokeFromNetError(e)
                    }
                })

        }
    }

    private fun onLoadPreviousJokeFromStorageSuccess(t: List<RoomJoke>) {
        if (t.isNotEmpty()) {
            viewState.showBtnBack()
            previousJokeId = t[0].id
        } else {
            previousJokeId = 0
            viewState.hideBtnBack()
        }
//        Log.d(
//            "Моя проверка",
//            "Получен ответ о возможности загрузить предыдущую шутку: всего элементов ${t.size} ид первого: ${t[0].id} ИД последнего ${t[t.size - 1].id}"
//        )


    }

    fun btnNextPressed() {
        loadNextJokeFromStorage()
//        //нажата кнопка петехода к следующей шутке.
//        previousJokeId = currentJokeId
//        viewState.showBtnBack()
//        loadJokesById(nextJokeId)

    }

    private fun loadJokesById(jokeId: Long) {
        category?.let {
            jokesRepository
                .getContentById(jokeId, it)
                .observeOn(schedulers.main())
                .subscribe(object : SingleObserver<List<RoomJoke>> {
                    override fun onSubscribe(d: Disposable) {
                        disposables.add(d)
                    }

                    override fun onSuccess(t: List<RoomJoke>) {
                        onLoadNewJokeFromStorageSuccess(t)
                    }

                    override fun onError(e: Throwable) {
                        onLoadNewJokeFromNetError(e)
                    }
                })

        }
    }

    private fun onLoadNewJokeFromStorageSuccess(t: List<RoomJoke>) {
        if (t.size > 1) {
            nextJokeId = t[1].id
            viewState.showBtnNext()
            currentJokeId = t[0].id
            viewState.setContent(t[0].content)
        } else {
            if (t.isEmpty()) viewState.hideBtnBack()
            viewState.hideBtnNext()
        }

        Log.d(
            "Моя проверка",
            "Получен ответ: всего элементов ${t.size} ид первого: ${t[0].id} ИД последнего ${t[t.size - 1].id}"
        )
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