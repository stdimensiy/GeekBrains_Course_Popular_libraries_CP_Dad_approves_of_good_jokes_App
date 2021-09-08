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

    @Query("SELECT COUNT(*) FROM Jokes WHERE content = :s")
    fun getCountByContent(s: String): Single<Int>
}