package com.android.myplacesandroid.presentation.screen.add_new_place_screen

import android.graphics.Bitmap
import com.android.myplacesandroid.domain.model.*
import com.google.android.gms.maps.model.LatLng

enum class PlaceInsertStatus {
    INSERTED,
    FAILED
}

data class AddNewPlaceState(
    val location: Location = Location(null),
    val title: Title = Title(""),
    val desc: Description = Description(""),
    val image: Bitmap? = null,
    val isLoading: Boolean = false,
    val showErrorMessages: Boolean = false,
    val insertStatus: PlaceInsertStatus? = null
)