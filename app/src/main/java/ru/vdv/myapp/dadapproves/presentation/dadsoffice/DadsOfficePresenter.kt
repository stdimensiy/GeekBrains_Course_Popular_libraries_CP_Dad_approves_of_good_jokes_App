package ru.vdv.myapp.dadapproves.presentation.dadsoffice

import android.util.Log
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.vdv.myapp.dadapproves.AndroidScreens
import ru.vdv.myapp.dadapproves.data.model.Group
import ru.vdv.myapp.dadapproves.data.model.JokesRepository
import ru.vdv.myapp.dadapproves.myschedulers.IMySchedulers
import ru.vdv.myapp.dadapproves.presentation.interfaces.MainFragmentView

class DadsOfficePresenter(
    private val schedulers: IMySchedulers,
    private val jokesRepository: JokesRepository,
    private val router: Router
) : MvpPresenter<MainFragmentView>() {
    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
    }

    override fun attachView(view: MainFragmentView?) {
        super.attachView(view)
        getApprovedAnecdotesCount()
        getApprovedStoriesCount()
        getApprovedPoemsCount()
        getApprovedAphorismsCount()
    }

    private fun getApprovedAphorismsCount() {
        disposables += Single.zip(
            jokesRepository.getCountApprovedJokesByCategoryId(Group.APHORISMS.id),
            jokesRepository.getCountByCategoryId(Group.APHORISMS.id),
            fun(t1, t2): String { return " $t1 из $t2" })
            .subscribeOn(schedulers.io())
            .subscribe(
                { viewState.setAphorismsCount("Одобрено $it афоризмов") },
                { onRepositoryRequestError(it) })
    }

    private fun getApprovedPoemsCount() {
        disposables += Single.zip(
            jokesRepository.getCountApprovedJokesByCategoryId(Group.POEMS.id),
            jokesRepository.getCountByCategoryId(Group.POEMS.id),
            fun(t1, t2): String { return " $t1 из $t2" })
            .subscribeOn(schedulers.io())
            .subscribe(
                { viewState.setPoemsCount("Одобрено $it стихотворений") },
                { onRepositoryRequestError(it) })
    }

    private fun getApprovedStoriesCount() {
        disposables += Single.zip(
            jokesRepository.getCountApprovedJokesByCategoryId(Group.STORIES.id),
            jokesRepository.getCountByCategoryId(Group.STORIES.id),
            fun(t1, t2): String { return " $t1 из $t2" })
            .subscribeOn(schedulers.io())
            .subscribe(
                { viewState.setStoriesCount("Одобрено $it рассказов") },
                { onRepositoryRequestError(it) })
    }

    private fun getApprovedAnecdotesCount() {
        disposables += Single.zip(
            jokesRepository.getCountApprovedJokesByCategoryId(Group.ANECDOTES.id),
            jokesRepository.getCountByCategoryId(Group.ANECDOTES.id),
            fun(t1, t2): String { return " $t1 из $t2" })
            .subscribeOn(schedulers.io())
            .subscribe(
                { viewState.setAnecdotesCount("Одобрено $it анекдотов") },
                { onRepositoryRequestError(it) })
    }

    private fun onRepositoryRequestError(e: Throwable) {
        Log.d("Моя проверка", "поток вернул ошибку: $e")

    }

    fun goToContentView(i: Int) {
        router.navigateTo(AndroidScreens().contentView(true, i))
    }

    fun backPressed(): Boolean {
        router.backTo(AndroidScreens().main())
        return true
    }

    override fun onDestroy() {
        disposables.clear()
    }
}