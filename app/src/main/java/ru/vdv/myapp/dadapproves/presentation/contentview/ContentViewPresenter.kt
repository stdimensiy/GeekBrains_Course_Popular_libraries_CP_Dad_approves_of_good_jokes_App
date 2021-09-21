package ru.vdv.myapp.dadapproves.presentation.contentview

import android.content.Context
import android.widget.Toast
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.vdv.myapp.dadapproves.R
import ru.vdv.myapp.dadapproves.data.model.Joke
import ru.vdv.myapp.dadapproves.data.model.JokesRepository
import ru.vdv.myapp.dadapproves.data.model.NetworkState
import ru.vdv.myapp.dadapproves.data.model.RoomJoke
import ru.vdv.myapp.dadapproves.data.net.NetworkStateObservable
import ru.vdv.myapp.dadapproves.myschedulers.IMySchedulers
import ru.vdv.myapp.dadapproves.presentation.interfaces.ContentView

class ContentViewPresenter(
    private val context: Context,
    private var moderationMode: Boolean = false,
    private val category: Int? = 1,
    private val jokesRepository: JokesRepository,
    private val schedulers: IMySchedulers,
    private val router: Router
) : MvpPresenter<ContentView>() {
    private var currentJoke: RoomJoke? = null
    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        viewState.hideModeratorBtnGroup()
        viewState.clearStatus()
        viewState.setTag(context.getString(R.string.main_greeting))
        if (moderationMode) {
            viewState.moderatorModeInit()
            viewState.showModeratorBtnGroup()
        }
        loadNextJokeFromStorage()
        disposables += NetworkStateObservable(context)
            .doOnNext { onNext(it) }
            .publish()
            .connect()
    }

    private fun onNext(networkState: NetworkState) {
        Toast.makeText(context, "Сеть: ${networkState.tag}", Toast.LENGTH_SHORT).show()
        when (networkState) {
            NetworkState.DISCONNECTED -> viewState.disableBtnLoadNewJokeFromNetwork()
            NetworkState.CONNECTED -> viewState.enableBtnLoadNewJokeFromNetwork()
        }

    }

    private fun loadNextJokeFromStorage() {
        category?.let { categoryId ->
            val apply = (if (moderationMode) {
                jokesRepository.getNextOne(categoryId, currentJoke?.id ?: 0)
            } else {
                jokesRepository.getNextOneApproves(categoryId, currentJoke?.id ?: 0)
            })
            disposables += apply.observeOn(schedulers.main())
                .subscribe(
                    { onLoadJokeFromStorageSuccess(it) },
                    { onLoadNewJokeFromStorageError(it) })
        }
    }

    private fun loadPreviousJokeFromStorage() {
        category?.let { categoryId ->
            val apply = (if (moderationMode) {
                jokesRepository.getPreviousOne(categoryId, currentJoke?.id ?: 0)
            } else {
                jokesRepository.getPreviousOneApproves(categoryId, currentJoke?.id ?: 0)
            })
            disposables += apply.observeOn(schedulers.main())
                .subscribe(
                    { onLoadJokeFromStorageSuccess(it) },
                    { onLoadNewJokeFromStorageError(it) })
        }
    }

    private fun onLoadJokeFromStorageSuccess(t: RoomJoke) {
        currentJoke = t
        // комплексная выдача результата на эеран
        displayingCurrentResult()
        //управление кнопками врепед и назад
        category?.let { categoryId ->
            val countPrevious = (if (moderationMode) {
                jokesRepository.getCountPrevious(categoryId, currentJoke?.id ?: 0)
            } else {
                jokesRepository.getCountApprovesPrevious(categoryId, currentJoke?.id ?: 0)
            })
            disposables += countPrevious.observeOn(schedulers.main())
                .subscribe({
                    if (it > 0) viewState.enableBtnBack()
                    else viewState.disableBtnBack()
                }, {
                    viewState.disableBtnBack()
                })
            val countNext = (if (moderationMode) {
                jokesRepository.getCountNext(categoryId, currentJoke?.id ?: 0)
            } else {
                jokesRepository.getCountApprovesNext(categoryId, currentJoke?.id ?: 0)
            })
            countNext.observeOn(schedulers.main())
                .subscribe(
                    {
                        if (it > 0) viewState.enableBtnNext()
                        else viewState.disableBtnNext()
                    },
                    { viewState.disableBtnNext() })
        }
    }

    private fun displayingCurrentResult() {
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
            if (!it.isApproved and !it.isForbidden) {
                viewState.showStatusNotVerified()
                viewState.enableBtnForbidden()
                viewState.enableBtnApprove()
            }
        }
    }


    private fun onLoadNewJokeFromNetError(e: Throwable) {
        Toast.makeText(context, context.getString(R.string.net_error) + " $e", Toast.LENGTH_SHORT)
            .show()
    }

    private fun onLoadNewJokeFromStorageError(e: Throwable) {
        Toast.makeText(
            context,
            context.getString(R.string.storage_error) + " $e",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onLoadNewJokeFromNetSuccess(t: Joke) {
        //сначала проверяем пришедший результат на уникальность
        disposables += jokesRepository
            .getCountByContent(t.content)
            .observeOn(schedulers.main())
            .subscribe({ joke ->
                if (joke > 0) {
                    loadNewJokeFromNet()
                } else {
                    viewState.setContent(t.content)
                    currentJoke = RoomJoke(t.content, 0, category)
                    currentJoke?.let { roomJoke ->
                        disposables += jokesRepository
                            .retainContent(roomJoke)
                            .observeOn(schedulers.main())
                            .subscribe({
                                roomJoke.id = it
                                viewState.enableBtnApprove()
                                viewState.enableBtnForbidden()
                                viewState.disableBtnNext()
                                viewState.enableBtnBack()
                            }, {
                                onLoadNewJokeFromNetError(it)
                            })
                    }
                }
            },
                {
                    Toast.makeText(
                        context,
                        context.getString(R.string.non_unique_content),
                        Toast.LENGTH_SHORT
                    ).show()
                    loadNextJokeFromStorage()
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

    fun loadNewJokeFromNet() {
        category?.let { categoryId ->
            disposables += jokesRepository
                .getContent(categoryId)
                .observeOn(schedulers.main())
                .subscribe({
                    onLoadNewJokeFromNetSuccess(it)
                }, {
                    onLoadNewJokeFromNetError(it)
                })
        }
    }

    private fun updateContent() {
        currentJoke?.let { roomJoke ->
            disposables += jokesRepository
                .updateContent(roomJoke)
                .observeOn(schedulers.main())
                .subscribe({
                    Toast.makeText(
                        context,
                        context.getString(R.string.changes_were_saved_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    if (roomJoke.isForbidden) viewState.disableBtnForbidden()
                    if (roomJoke.isApproved) viewState.disableBtnApprove()
                }, {
                    onLoadNewJokeFromNetError(it)
                })
        }
    }

    override fun onDestroy() {
        disposables.clear()
    }
}