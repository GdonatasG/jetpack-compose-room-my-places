package com.android.myplacesandroid.data.local

import com.android.myplacesandroid.domain.model.Description
import com.android.myplacesandroid.domain.model.MyPlace
import com.android.myplacesandroid.domain.model.Title

fun MyPlaceEntity.toDomain(): MyPlace {
    return MyPlace(
        lat = lat,
        lng = lng,
        title = Title(title),
        description = Description(description),
        image = image,
        id = id
    )
}

fun MyPlace.fromDomain(): MyPlaceEntity {
    return MyPlaceEntity(
        lat = lat,
        lng = lng,
        title = title.getOrCrash(),
        description = description.getOrCrash(),
        image = image,
        id = id
    )
}