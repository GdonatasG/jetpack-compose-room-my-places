package com.android.myplacesandroid.presentation.screen.root.home

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.myplacesandroid.presentation.utils.rememberMultiplePermissionsStateWithLifeCycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("MissingPermission")
@OptIn(
    ExperimentalPermissionsApi::class
)
@Destination(start = true)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val isMapExpanded = rememberSaveable { mutableStateOf(false) }
    val isListExpanded = rememberSaveable { mutableStateOf(false) }
    val permissionsState = rememberMultiplePermissionsStateWithLifeCycle(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(text = "My Places")
                    },
                )
            }
        }
    ) {
        if (viewModel.state.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.testTag(HomeScreenTestTags.CIRCULAR_PROGRESS_INDICATOR)
                )
            }
        } else {
            Column(modifier = Modifier.testTag(HomeScreenTestTags.LOADED_VIEW)) {
                Box(
                    modifier = Modifier
                        .weight(
                            if (!permissionsState.allPermissionsGranted) {
                                1f
                            } else {
                                if (isMapExpanded.value) {
                                    4f
                                } else {
                                    1f
                                }
                            }
                        )
                        .testTag(HomeScreenTestTags.MAP_TAB_BOX)
                ) {
                    MapTabScreen(viewModel = viewModel, permissionsState = permissionsState)
                    if (permissionsState.allPermissionsGranted) {
                        SizeControls(isMapExpanded, isListExpanded)
                    }
                }
                Divider(thickness = 0.8.dp)
                Box(
                    modifier = Modifier
                        .weight(
                            if (!permissionsState.allPermissionsGranted) {
                                3f
                            } else {
                                if (isListExpanded.value) {
                                    4f
                                } else {
                                    1f
                                }
                            }
                        )
                        .testTag(HomeScreenTestTags.LIST_TAB_BOX)
                ) {
                    ListTabScreen(viewModel.state.places)
                }

            }
        }

    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SizeControls(
    isMapExpanded: MutableState<Boolean>,
    isListExpanded: MutableState<Boolean>,
) {
    Column(
        Modifier
            .padding(horizontal = 10.dp, vertical = 12.dp)
    ) {
        IconButton(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.8f))
                .testTag(HomeScreenTestTags.SIZE_CONTROL_UP),
            onClick = {
                if (isMapExpanded.value) {
                    isMapExpanded.value = false
                } else {
                    isListExpanded.value = true
                }
            },
            enabled = isMapExpanded.value || !isListExpanded.value
        ) {
            Icon(Icons.Default.KeyboardArrowUp, null)
        }
        IconButton(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.8f))
                .testTag(HomeScreenTestTags.SIZE_CONTROL_DOWN),
            onClick = {
                if (isListExpanded.value) {
                    isListExpanded.value = false
                } else {
                    isMapExpanded.value = true
                }
            },
            enabled = isListExpanded.value || !isMapExpanded.value
        ) {
            Icon(Icons.Default.KeyboardArrowDown, null)
        }
    }
}

/*
@SuppressLint("MissingPermission")
fun getDeviceLocation(context: Context, viewModel: HomeViewModel) {
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
        context
    )

    fusedLocationProviderClient.lastLocation.addOnComplteListener {
        val lastKnownLocation = it.result
        if (lastKnownLocation != null) {
            viewModel.setDeviceLocation(
                LatLng(
                    lastKnownLocation.latitude,
                    lastKnownLocation.longitude
                )
            )
        }
    }
}*/
