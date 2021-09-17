package ru.vdv.myapp.dadapproves.data.model

import android.util.Log
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
        return db.storageDao().getCountByCategoryId(groupId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getCountApprovedJokesByCategoryId(groupId: Int): Single<Int> {
        Log.d(
            "Моя проверка / репозиторий",
            "пошел запрос к базе в части определения количества одобренных шуток категории $groupId"
        )
        return db.storageDao().getCountApprovedJokesByCategoryId(groupId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    override fun retainContent(joke: RoomJoke): Single<Long> {
        Log.d("Моя проверка / репозиторий", "Поытка записи значений в базу")
        return db.storageDao().insert(joke).subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getCountByContent(s: String): Single<Int> {
        Log.d(
            "Моя проверка / репозиторий",
            "Поытка попытка поиска записи в базе по значению поля content"
        )
        return db.storageDao().getCountByContent(s).subscribeOn(MySchedulersFactory.create().io())
    }

    override fun updateContent(joke: RoomJoke): Single<Int> {
        Log.d("Моя проверка / репозиторий", "Поытка записи значений обновленных данных в базу")
        return db.storageDao().update(joke).subscribeOn(MySchedulersFactory.create().io())
    }

    // получить следующий одобренный
    override fun getNextOneApproves(groupId: Int, jokeId: Long): Single<RoomJoke> {
        Log.d("Моя проверка / репозиторий", "Пытаюсь получить следующий одобренный")
        return db.storageDao()
            .getNextOneApproves(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    // получить предыдущий одобренный
    override fun getPreviousOneApproves(groupId: Int, jokeId: Long): Single<RoomJoke> {
        Log.d("Моя проверка / репозиторий", "Пытаюсь получить предыдущий одобренный")
        return db.storageDao()
            .getPreviousOneApproves(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    // получить следующий (без учета критерия одобрения)
    override fun getNextOne(groupId: Int, jokeId: Long): Single<RoomJoke> {
        Log.d("Моя проверка / репозиторий", "Пытаюсь получить следующий")
        return db.storageDao()
            .getNextOne(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    // получить предыдущий (без учета критерия одобрения)
    override fun getPreviousOne(groupId: Int, jokeId: Long): Single<RoomJoke> {
        Log.d("Моя проверка / репозиторий", "Пытаюсь получить предыдущий по текущему индексу $jokeId")
        return db.storageDao()
            .getPreviousOne(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getCountPrevious(groupId: Int, jokeId: Long): Single<Int> {
        Log.d("Моя проверка / репозиторий", "Пытаюсь получить количество шуток предшевствующих текущей")
        return db.storageDao()
            .getCountPrevious(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getCountNext(groupId: Int, jokeId: Long): Single<Int> {
        Log.d("Моя проверка / репозиторий", "Пытаюсь получить количество шуток пследующих за текущей")
        return db.storageDao()
            .getCountNext(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getCountApprovesPrevious(groupId: Int, jokeId: Long): Single<Int> {
        Log.d("Моя проверка / репозиторий", "Пытаюсь получить количество ОДОБРЕННЫХ шуток предшевствующих текущей")
        return db.storageDao()
            .getCountApprovesPrevious(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getCountApprovesNext(groupId: Int, jokeId: Long): Single<Int> {
        Log.d("Моя проверка / репозиторий", "Пытаюсь получить количество ОДОБРЕННЫХ уток пследующих за текущей")
        return db.storageDao()
            .getCountApprovesNext(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }
}