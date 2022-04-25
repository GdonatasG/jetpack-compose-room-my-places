package com.android.myplacesandroid.di

import androidx.compose.runtime.Composable
import com.android.myplacesandroid.presentation.utils.rememberAppState

class DependencyContainer {
    val getAppState
        @Composable get() = rememberAppState()
}