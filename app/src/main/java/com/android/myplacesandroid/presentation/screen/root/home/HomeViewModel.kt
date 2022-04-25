package com.android.myplacesandroid.presentation.screen.root.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.myplacesandroid.OpenForTesting
import com.android.myplacesandroid.domain.repository.MyPlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OpenForTesting
class HomeViewModel @Inject constructor(
    private val myPlaceRepository: MyPlaceRepository
) : ViewModel() {
    private var _state by mutableStateOf(HomeState())
    val state: HomeState
        get() = _state

    init {
        viewModelScope.launch {
            _state = _state.copy(
                isLoading = true
            )
            myPlaceRepository.getAllPlaces().collectLatest {
                _state = _state.copy(
                    places = it,
                    isLoading = false
                )
            }
        }
    }


    fun onEvent(event: HomeEvent) {
    }

}