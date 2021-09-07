package ru.vdv.myapp.dadapproves.data.model

import android.util.Log
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.vdv.myapp.dadapproves.data.retrofit.IDataRNAPI
import ru.vdv.myapp.dadapproves.data.storage.MyStorage
import ru.vdv.myapp.dadapproves.myschedulers.MySchedulersFactory

class JokesRepository(private val api: IDataRNAPI, private val db: MyStorage) : IJokesRepository {
    override fun getContent(groupId: Int): Single<Joke> {
        Log.d("Моя проверка / репозиторий", "процесс пошел...")
        return api.getContent(groupId).subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getCountByCategoryId(groupId: Int): Single<Int> {
        Log.d("Моя проверка / репозиторий", "пошел запрос к базе")
        return db.storageDao().getCountByCategoryId(groupId).subscribeOn(MySchedulersFactory.create().io())
    }

    override fun retainContent(joke: RoomJoke): Single<Long>  {
        Log.d("Моя проверка / репозиторий", "Поытка записи значений в базу")
        return db.storageDao().insert(joke).subscribeOn(MySchedulersFactory.create().io())
    }
}