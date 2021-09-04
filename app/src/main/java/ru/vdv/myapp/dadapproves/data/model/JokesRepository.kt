package ru.vdv.myapp.dadapproves.data.model

import android.util.Log
import io.reactivex.rxjava3.core.Single
import ru.vdv.myapp.dadapproves.data.retrofit.IDataRNAPI
import ru.vdv.myapp.dadapproves.myschedulers.MySchedulersFactory

class JokesRepository(private val api: IDataRNAPI) : IJokesRepository {
    override fun getContent(groupId: Int): Single<Joke> {
        Log.d("Моя проверка / репозиторий", "процесс пошел...")
        return api.getContent(groupId).subscribeOn(MySchedulersFactory.create().io())
    }
}