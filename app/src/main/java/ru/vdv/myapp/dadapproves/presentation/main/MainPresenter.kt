package ru.vdv.myapp.dadapproves.presentation.main

import android.content.Context
import android.widget.Toast
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import moxy.MvpPresenter
import ru.vdv.myapp.dadapproves.AndroidScreens
import ru.vdv.myapp.dadapproves.R
import ru.vdv.myapp.dadapproves.data.model.JokesRepository
import ru.vdv.myapp.dadapproves.myschedulers.IMySchedulers
import ru.vdv.myapp.dadapproves.presentation.interfaces.MainFragmentView

class MainPresenter(
    private val context: Context,
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
        disposables += jokesRepository.getCountApprovedJokesByCategoryId(4)
            .observeOn(schedulers.main())
            .subscribe(
                { viewState.setAphorismsCount("Папой одобрено $it афоризмов") },
                { onRepositoryRequestError(it) })
    }

    private fun getApprovedPoemsCount() {
        disposables += jokesRepository.getCountApprovedJokesByCategoryId(3)
            .observeOn(schedulers.main())
            .subscribe(
                { viewState.setPoemsCount("Папой одобрено $it стихотворений") },
                { onRepositoryRequestError(it) })
    }

    private fun getApprovedStoriesCount() {
        disposables += jokesRepository.getCountApprovedJokesByCategoryId(2)
            .observeOn(schedulers.main())
            .subscribe(
                { viewState.setStoriesCount("Папой одобрено $it рассказов") },
                { onRepositoryRequestError(it) })
    }

    private fun getApprovedAnecdotesCount() {
        disposables += jokesRepository.getCountApprovedJokesByCategoryId(1)
            .observeOn(schedulers.main())
            .subscribe(
                { viewState.setAnecdotesCount("Папой одобрено $it анекдотов") },
                { onRepositoryRequestError(it) })
    }

    private fun onRepositoryRequestError(e: Throwable) {
        Toast.makeText(
            context,
            context.getString(R.string.storage_error) + " $e",
            Toast.LENGTH_SHORT
        ).show()
    }

    fun goToDadLock() {
        router.navigateTo(AndroidScreens().dadLock())
    }

    fun goToContentView(i: Int) {
        router.navigateTo(AndroidScreens().contentView(false, i))
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }

    override fun onDestroy() {
        disposables.clear()
    }
}