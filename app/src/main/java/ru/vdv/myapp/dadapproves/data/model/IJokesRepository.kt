package ru.vdv.myapp.dadapproves.data.model

import io.reactivex.rxjava3.core.Single

/**
 * Интерфейс репозитория шуток
 *
 * сетевой ресурс RzhuNemagu.ru
 * локальный ресурс - база данных через room
 * функционально общий интерфейс репозитория
 */
interface IJokesRepository {

    /**
     * Позволяет получить  / загрузить / новую шутку с внешнего сервера
     */
    fun getContent(groupId: Int): Single<Joke>

    /**
     * Позволяет получить общее количество элементов заданной категории
     * (без учета критерия одобрения или блокировки)
     */
    fun getCountByCategoryId(groupId: Int): Single<Int>

    /**
     * Позволяет получить общее количество элементов заданной категории
     * имеющих признак  - "ОДОБРЕН"
     */
    fun getCountApprovedJokesByCategoryId(groupId: Int): Single<Int>

    /**
     * Позволяет сохранить новый элемент в базу данных
     */
    fun retainContent(joke: RoomJoke): Single<Long>

    /**
     * Позволяет получить количество элементов содержащих контент совпадающий с переданным
     * в параметре (применяется для отслеживания полных дублей записей)
     */
    fun getCountByContent(s: String): Single<Int>

    /**
     * Позволяет сохранить измененный злемент (обновить) в базу данных
     */
    fun updateContent(joke: RoomJoke): Single<Int>

    /**
     * Позволяет получить следующий элемент имеющий признак  - "ОДОБРЕН"
     */
    fun getNextOneApproves(groupId: Int, jokeId: Long): Single<RoomJoke>

    /**
     * Позволяет получить предыдущий элемент имеющий признак  - "ОДОБРЕН"
     */
    fun getPreviousOneApproves(groupId: Int, jokeId: Long): Single<RoomJoke>

    /**
     * Позволяет получить следующий элемент (без учета критерия одобрения или блокировки)
     */
    fun getNextOne(groupId: Int, jokeId: Long): Single<RoomJoke>

    /**
     * Позволяет получить предыдущий элемент (без учета критерия одобрения или блокировки)
     */
    fun getPreviousOne(groupId: Int, jokeId: Long): Single<RoomJoke>

    /**
     * Позволяет получить количество элементов предшевствующих текущему
     * (без учета критерия одобрения или блокировки)
     */
    fun getCountPrevious(groupId: Int, jokeId: Long): Single<Int>

    /**
     * Позволяет получить количество элементов следующих за текущим
     * (без учета критерия одобрения или блокировки)
     */
    fun getCountNext(groupId: Int, jokeId: Long): Single<Int>

    /**
     * Позволяет получить количество элементов предшевствующих текущему
     * и имеющих признак  - "ОДОБРЕН"
     */
    fun getCountApprovesPrevious(groupId: Int, jokeId: Long): Single<Int>

    /**
     * Позволяет получить количество элементов следующих за текущим
     * и имеющих признак  - "ОДОБРЕН"
     */
    fun getCountApprovesNext(groupId: Int, jokeId: Long): Single<Int>
}