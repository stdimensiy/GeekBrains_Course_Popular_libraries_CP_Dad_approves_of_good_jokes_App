package ru.vdv.myapp.dadapproves.data.storage

import androidx.room.*
import io.reactivex.rxjava3.core.Single
import ru.vdv.myapp.dadapproves.data.model.RoomJoke

/**
 * Интерфейс доступа к базе данных (DAO - Data Access Object)
 */
@Dao
interface StorageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(joke: RoomJoke): Single<Long>

    @Update
    fun update(joke: RoomJoke): Single<Int>

    @Delete
    fun delete(joke: RoomJoke)

    @Query("SELECT COUNT(*) FROM Jokes WHERE type = :categoryId")
    fun getCountByCategoryId(categoryId: Int): Single<Int>

    @Query("SELECT COUNT(*) FROM Jokes WHERE type = :categoryId AND isApproved = 1")
    fun getCountApprovedJokesByCategoryId(categoryId: Int): Single<Int>

    @Query("SELECT COUNT(*) FROM Jokes WHERE content = :s")
    fun getCountByContent(s: String): Single<Int>

    @Query("SELECT * FROM Jokes WHERE type = :categoryId AND id > :jokeId AND isApproved = 1 LIMIT 1")
    fun getNextOneApproves(categoryId: Int, jokeId: Long): Single<RoomJoke>

    @Query("SELECT * FROM Jokes WHERE type = :categoryId AND id < :jokeId AND isApproved = 1 ORDER BY id DESC LIMIT 1")
    fun getPreviousOneApproves(categoryId: Int, jokeId: Long): Single<RoomJoke>

    @Query("SELECT * FROM Jokes WHERE type = :categoryId AND id > :jokeId LIMIT 1")
    fun getNextOne(categoryId: Int, jokeId: Long): Single<RoomJoke>

    @Query("SELECT * FROM Jokes WHERE type = :categoryId AND id < :jokeId ORDER BY id DESC LIMIT 1")
    fun getPreviousOne(categoryId: Int, jokeId: Long): Single<RoomJoke>

    @Query("SELECT COUNT(*) FROM Jokes WHERE type = :categoryId AND id < :jokeId")
    fun getCountPrevious(categoryId: Int, jokeId: Long): Single<Int>

    @Query("SELECT COUNT(*) FROM Jokes WHERE type = :categoryId AND id < :jokeId AND isApproved = 1")
    fun getCountApprovesPrevious(categoryId: Int, jokeId: Long): Single<Int>

    @Query("SELECT COUNT(*) FROM Jokes WHERE type = :categoryId AND id > :jokeId")
    fun getCountNext(categoryId: Int, jokeId: Long): Single<Int>

    @Query("SELECT COUNT(*) FROM Jokes WHERE type = :categoryId AND id > :jokeId AND isApproved = 1")
    fun getCountApprovesNext(categoryId: Int, jokeId: Long): Single<Int>
}