package com.android.myplacesandroid.presentation.screen.root.home

import com.android.myplacesandroid.domain.model.MyPlace

data class HomeState(
    val places: List<MyPlace> = emptyList(),
    val isLoading: Boolean = false
)
