package ru.vdv.myapp.dadapproves.data.model

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Интерфейс репозитория (пока ориентирован на взаимодействие с ресурсом RzhuNemagu.ru
 * функционально общий интерфейс репозитория
 */
interface IJokesRepository {
    fun getContent(groupId: Int): Single<Joke>
    fun getCountByCategoryId(groupId: Int): Single<Int>
    fun retainContent(joke: RoomJoke): Single<Long>
}