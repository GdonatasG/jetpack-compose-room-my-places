package com.android.myplacesandroid.presentation.screen.add_new_place_screen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.myplacesandroid.domain.model.Description
import com.android.myplacesandroid.domain.model.Location
import com.android.myplacesandroid.domain.model.MyPlace
import com.android.myplacesandroid.domain.model.Title
import com.android.myplacesandroid.domain.repository.MyPlaceRepository
import com.android.myplacesandroid.utils.BitmapUtils.convertBitmapToByteArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class AddNewPlaceViewModel @Inject constructor(
    private val repository: MyPlaceRepository
) : ViewModel() {
    private var _state by mutableStateOf(AddNewPlaceState())
    val state: AddNewPlaceState
        get() = _state

    fun onEvent(event: AddNewPlaceEvent) {
        when (event) {
            is AddNewPlaceEvent.AddPlace -> {
                if (_state.title.isValid() && _state.desc.isValid() && _state.location.isValid()) {
                    _state = _state.copy(showErrorMessages = false)
                    viewModelScope.launch {
                        try {
                            val latLng = _state.location.getOrCrash()
                            val title = _state.title.getOrCrash()
                            val desc = _state.desc.getOrCrash()
                            var image = ByteArray(size = 0)
                            _state.image?.apply {
                                image = convertBitmapToByteArray(this)
                            }
                            repository.insertPlace(
                                MyPlace(
                                    lat = latLng!!.latitude,
                                    lng = latLng.longitude,
                                    title = Title(title),
                                    description = Description(desc),
                                    image = image
                                )
                            )
                            _state = _state.copy(insertStatus = PlaceInsertStatus.INSERTED)
                        } catch (e: Exception) {
                            if (e is CancellationException) {
                                return@launch
                            }
                            _state = _state.copy(insertStatus = PlaceInsertStatus.FAILED)
                            Log.e("${AddNewPlaceViewModel::class.simpleName}", e.message.toString())
                        }
                    }

                    if (_state.showErrorMessages) {
                        _state = _state.copy(showErrorMessages = false)
                    }
                } else {
                    _state = _state.copy(showErrorMessages = true)
                }
            }
            is AddNewPlaceEvent.LocationChanged -> {
                _state = _state.copy(location = Location(event.location))
            }
            is AddNewPlaceEvent.TitleChanged -> {
                _state = _state.copy(title = Title(event.title))
            }
            is AddNewPlaceEvent.DescriptionChanged -> {
                _state = _state.copy(desc = Description(event.desc))
            }
            is AddNewPlaceEvent.ImageChanged -> {
                _state = _state.copy(image = event.image)
            }
            is AddNewPlaceEvent.ImageRemoved -> {
                _state = _state.copy(image = null)
            }
            is AddNewPlaceEvent.ClearInsertStatus -> {
                _state = _state.copy(insertStatus = null)
            }
        }
    }

}