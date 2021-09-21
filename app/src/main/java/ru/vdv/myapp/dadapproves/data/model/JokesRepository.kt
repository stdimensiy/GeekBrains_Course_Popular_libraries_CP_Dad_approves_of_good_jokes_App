package ru.vdv.myapp.dadapproves.data.model

import io.reactivex.rxjava3.core.Single
import ru.vdv.myapp.dadapproves.data.retrofit.IDataRNAPI
import ru.vdv.myapp.dadapproves.data.storage.MyStorage
import ru.vdv.myapp.dadapproves.myschedulers.MySchedulersFactory

class JokesRepository(private val api: IDataRNAPI, private val db: MyStorage) : IJokesRepository {
    override fun getContent(groupId: Int): Single<Joke> {
        return api.getContent(groupId).subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getCountByCategoryId(groupId: Int): Single<Int> {
        return db.storageDao().getCountByCategoryId(groupId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getCountApprovedJokesByCategoryId(groupId: Int): Single<Int> {
        return db.storageDao().getCountApprovedJokesByCategoryId(groupId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    override fun retainContent(joke: RoomJoke): Single<Long> {
        return db.storageDao().insert(joke).subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getCountByContent(s: String): Single<Int> {
        return db.storageDao().getCountByContent(s).subscribeOn(MySchedulersFactory.create().io())
    }

    override fun updateContent(joke: RoomJoke): Single<Int> {
        return db.storageDao().update(joke).subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getNextOneApproves(groupId: Int, jokeId: Long): Single<RoomJoke> {
        return db.storageDao()
            .getNextOneApproves(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getPreviousOneApproves(groupId: Int, jokeId: Long): Single<RoomJoke> {
        return db.storageDao()
            .getPreviousOneApproves(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getNextOne(groupId: Int, jokeId: Long): Single<RoomJoke> {
        return db.storageDao()
            .getNextOne(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getPreviousOne(groupId: Int, jokeId: Long): Single<RoomJoke> {
        return db.storageDao()
            .getPreviousOne(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getCountPrevious(groupId: Int, jokeId: Long): Single<Int> {
        return db.storageDao()
            .getCountPrevious(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getCountNext(groupId: Int, jokeId: Long): Single<Int> {
        return db.storageDao()
            .getCountNext(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getCountApprovesPrevious(groupId: Int, jokeId: Long): Single<Int> {
        return db.storageDao()
            .getCountApprovesPrevious(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }

    override fun getCountApprovesNext(groupId: Int, jokeId: Long): Single<Int> {
        return db.storageDao()
            .getCountApprovesNext(groupId, jokeId)
            .subscribeOn(MySchedulersFactory.create().io())
    }
}