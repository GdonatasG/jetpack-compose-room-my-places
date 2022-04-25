package com.android.myplacesandroid.presentation.screen.add_new_place_screen

import android.Manifest
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.navArgument
import com.android.myplacesandroid.presentation.utils.rememberMultiplePermissionsStateWithLifeCycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import java.util.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
@Destination(
    navArgsDelegate = LocationSelectorMapScreenNavArgs::class
)
fun LocationSelectorMapScreen(
    resultNavigator: ResultBackNavigator<LatLng>,
    navArgs: LocationSelectorMapScreenNavArgs
) {
    val permissionsState = rememberMultiplePermissionsStateWithLifeCycle(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    val selectedLocation: MutableState<LatLng?> = rememberSaveable {
        mutableStateOf(navArgs.latLng)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        resultNavigator.navigateBack()

                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Back")
                    }
                },
                actions = {
                    selectedLocation.value?.apply {
                        TextButton(
                            onClick = {
                                resultNavigator.navigateBack(this)
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "SELECT")

                        }
                    }
                }
            )
        }
    ) {
        if (permissionsState.allPermissionsGranted) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp),
                    text = "Hold on specific map location to select",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    uiSettings = MapUiSettings(),
                    properties = MapProperties(isMyLocationEnabled = true),
                    onMapLongClick = {
                        selectedLocation.value = it
                    }
                ) {
                    selectedLocation.value?.apply {
                        Marker(state = MarkerState(position = this))
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Please enable Location Permissions to see your places on map." +
                            "\nIf permissions request dialog didn't pop, " +
                            "then go to your phone settings and enable Location Permissions manually",
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}