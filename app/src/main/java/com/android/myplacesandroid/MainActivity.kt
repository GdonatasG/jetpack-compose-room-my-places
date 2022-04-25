package com.android.myplacesandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.android.myplacesandroid.di.DependencyContainer
import com.android.myplacesandroid.presentation.screen.MainScreen
import com.android.myplacesandroid.presentation.theme.MyPlacesAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

val LocalDependencyContainer = staticCompositionLocalOf<DependencyContainer> {
    error("No dependency container provided!")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val dependencyContainer = DependencyContainer()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPlacesAndroidTheme {
                CompositionLocalProvider(LocalDependencyContainer provides dependencyContainer) {
                    MainScreen()
                }
            }
        }
    }
}