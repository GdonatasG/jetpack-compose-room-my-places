package com.android.myplacesandroid.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MyPlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: MyPlaceEntity)

    @Delete
    suspend fun deletePlace(place: MyPlaceEntity)

    @Query("SELECT * FROM my_places ORDER BY id DESC")
    fun getAllPlaces(): Flow<List<MyPlaceEntity>>
}