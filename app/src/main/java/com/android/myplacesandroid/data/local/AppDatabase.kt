package com.android.myplacesandroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MyPlaceEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val myPlaceDao: MyPlaceDao
}