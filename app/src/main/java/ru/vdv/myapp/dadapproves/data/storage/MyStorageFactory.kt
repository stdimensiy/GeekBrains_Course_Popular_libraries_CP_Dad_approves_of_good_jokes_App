package ru.vdv.myapp.dadapproves.data.storage

import android.content.Context
import androidx.room.Room

object MyStorageFactory {
    fun create(context: Context): MyStorage =
        Room.databaseBuilder(context, MyStorage::class.java, "jokes.db")
            .fallbackToDestructiveMigration()
            .build()
}