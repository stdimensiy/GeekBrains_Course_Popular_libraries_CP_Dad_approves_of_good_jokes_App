package ru.vdv.myapp.dadapproves.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.vdv.myapp.dadapproves.data.model.RoomJoke

@Database(exportSchema = false, entities = [RoomJoke::class], version = 1)
abstract class MyStorage : RoomDatabase() {
    abstract fun storageDao(): StorageDao
}