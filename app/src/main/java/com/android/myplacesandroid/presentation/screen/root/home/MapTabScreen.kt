package com.android.myplacesandroid.presentation.screen.root.home

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.android.myplacesandroid.utils.BitmapUtils.convertCompressedByteArrayToBitmap
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapTabScreen(viewModel: HomeViewModel, permissionsState: MultiplePermissionsState) {
    if (permissionsState.allPermissionsGranted) {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .testTag(MapTabScreenTestTags.GOOGLE_MAP),
            uiSettings = MapUiSettings(),
            properties = MapProperties(isMyLocationEnabled = true),
        ) {
            viewModel.state.places.forEach { place ->
                MarkerInfoWindowContent(
                    state = MarkerState(position = LatLng(place.lat, place.lng)),
                    onInfoWindowClick = {
                        // COULD NAVIGATE TO DETAILS
                    }
                ) {
                    var bitmap: Bitmap? = null
                    try {
                        bitmap = convertCompressedByteArrayToBitmap(place.image)
                    } catch (e: Exception) {
                    }
                    Column(
                        modifier = if (bitmap != null) {
                            Modifier
                                .width(80.dp)
                                .height(80.dp)
                                .padding(4.dp)
                        } else {
                            Modifier
                                .width(80.dp)
                                .padding(4.dp)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = place.title.getDataOrFailedValue() ?: "",
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        bitmap?.let {
                            Box(
                                Modifier
                                    .width(70.dp)
                                    .height(70.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(bitmap = it.asImageBitmap(), contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
                .testTag(MapTabScreenTestTags.PERMISSIONS_ERROR_VIEW),
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
