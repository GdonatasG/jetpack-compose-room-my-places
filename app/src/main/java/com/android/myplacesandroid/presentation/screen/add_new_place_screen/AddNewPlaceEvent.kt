package com.android.myplacesandroid.presentation.screen.add_new_place_screen

import android.graphics.Bitmap
import com.google.android.gms.maps.model.LatLng

sealed class AddNewPlaceEvent {
    object AddPlace : AddNewPlaceEvent()
    data class LocationChanged(val location: LatLng) : AddNewPlaceEvent()
    data class TitleChanged(val title: String) : AddNewPlaceEvent()
    data class DescriptionChanged(val desc: String) : AddNewPlaceEvent()
    data class ImageChanged(val image: Bitmap) : AddNewPlaceEvent()
    object ImageRemoved : AddNewPlaceEvent()
    object ClearInsertStatus: AddNewPlaceEvent()
}