package ru.vdv.myapp.dadapproves.data.storage

import androidx.room.*
import ru.vdv.myapp.dadapproves.data.model.RoomJoke

/**
 * Интерфейс доступа к базе данных (DAO - Data Access Object)
 */

interface StorageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(joke: RoomJoke)

    @Update
    fun update(joke: RoomJoke)

    @Delete
    fun delete(joke: RoomJoke)

    @Query("SELECT COUNT(*) FROM Jokes WHERE type = :categoryId")
    fun getCountByCategoryId(categoryId: Int)
}