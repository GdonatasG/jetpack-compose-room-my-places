package com.android.myplacesandroid.data.local

import com.android.myplacesandroid.domain.model.MyPlace
import com.android.myplacesandroid.domain.repository.MyPlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MyPlaceRepositoryImpl(
    private val dao: MyPlaceDao
) : MyPlaceRepository {
    override suspend fun insertPlace(place: MyPlace) {
        dao.insertPlace(place.fromDomain())
    }

    override suspend fun deletePlace(place: MyPlace) {
        dao.deletePlace(place.fromDomain())
    }

    override fun getAllPlaces(): Flow<List<MyPlace>> {
        return dao.getAllPlaces().map { places ->
            places.map { it.toDomain() }
        }
    }

}