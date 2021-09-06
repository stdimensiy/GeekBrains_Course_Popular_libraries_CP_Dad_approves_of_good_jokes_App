package ru.vdv.myapp.dadapproves.presentation.main

import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import ru.vdv.myapp.dadapproves.AndroidScreens
import ru.vdv.myapp.dadapproves.data.model.JokesRepository
import ru.vdv.myapp.dadapproves.myschedulers.IMySchedulers
import ru.vdv.myapp.dadapproves.presentation.interfaces.MainFragmentView
import ru.vdv.myapp.dadapproves.presentation.interfaces.MainView

class MainPresenter(
    private val schedulers: IMySchedulers,
    private val jokesRepository: JokesRepository,
    private val router: Router
) : MvpPresenter<MainFragmentView>() {

    val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.init()
        jokesRepository.getCountByCategoryId(1)
            .observeOn(schedulers.main())
            .subscribe(object : SingleObserver<Int>{
                override fun onSubscribe(d: Disposable) {
                    disposables.add(d)
                }

                override fun onSuccess(t: Int) {
                    viewState.setAnecdotesCount(t)
                }

                override fun onError(e: Throwable) {
                    TODO("Not yet implemented")
                }
            })
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