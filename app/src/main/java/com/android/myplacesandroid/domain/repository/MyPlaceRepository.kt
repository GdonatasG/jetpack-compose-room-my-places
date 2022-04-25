package com.android.myplacesandroid.domain.repository

import com.android.myplacesandroid.domain.model.MyPlace
import kotlinx.coroutines.flow.Flow

interface MyPlaceRepository {
    suspend fun insertPlace(place: MyPlace)

    suspend fun deletePlace(place: MyPlace)

    fun getAllPlaces(): Flow<List<MyPlace>>
}