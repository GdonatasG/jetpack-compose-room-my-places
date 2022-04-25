package com.android.myplacesandroid.presentation.screen.add_new_place_screen

import android.Manifest
import android.graphics.ImageDecoder
import android.os.Build.VERSION.SDK_INT
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.myplacesandroid.domain.core.ValueError
import com.android.myplacesandroid.presentation.screen.destinations.LocationSelectorMapScreenDestination
import com.android.myplacesandroid.presentation.utils.OutlinedTextFieldValidation
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.model.LatLng
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultRecipient
import com.ramcosta.composedestinations.result.getOr
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Destination
@Composable
fun AddNewPlaceScreen(
    navigator: DestinationsNavigator,
    locationResultRecipient: ResultRecipient<LocationSelectorMapScreenDestination, LatLng>,
    viewModel: AddNewPlaceViewModel = hiltViewModel()
) {
    val formScrollState = rememberScrollState()
    val scaffoldState = rememberScaffoldState()
    val snackBarCoroutineScope = rememberCoroutineScope()

    viewModel.state.insertStatus?.apply {
        when (this) {
            PlaceInsertStatus.INSERTED -> {
                navigator.navigateUp()
            }
            PlaceInsertStatus.FAILED -> {
                snackBarCoroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("An Error occurred while adding place!")
                }
                viewModel.onEvent(AddNewPlaceEvent.ClearInsertStatus)
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "New Place")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigator.navigateUp()

                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(formScrollState)
        ) {
            PlaceImageSelector(viewModel, scaffoldState, snackBarCoroutineScope)
            LocationSelector(viewModel, navigator, locationResultRecipient)
            OutlinedTextFieldValidation(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.state.title.value.data ?: viewModel.state.title.value.failedValue
                ?: "",
                label = {
                    Text(text = "Title")
                },
                error = setStringFromCallback {
                    val error = viewModel.state.title.value.error
                    if (error != null && viewModel.state.showErrorMessages) {
                        when (error) {
                            ValueError.INVALID_TITLE -> {
                                return@setStringFromCallback "Must be from ${viewModel.state.title.minLength} to ${viewModel.state.title.maxLength} symbols"
                            }
                            else -> {
                                /* NO-OP */
                            }
                        }
                    }
                    return@setStringFromCallback ""
                },
                onValueChange = {
                    viewModel.onEvent(AddNewPlaceEvent.TitleChanged(it))
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            OutlinedTextFieldValidation(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.state.desc.value.data ?: viewModel.state.desc.value.failedValue
                ?: "",
                label = {
                    Text(text = "Description")
                },
                singleLine = false,
                maxLines = 4,
                error = setStringFromCallback {
                    val error = viewModel.state.desc.value.error
                    if (error != null && viewModel.state.showErrorMessages) {
                        when (error) {
                            ValueError.INVALID_DESCRIPTION -> {
                                return@setStringFromCallback "Must be less or equal to ${viewModel.state.desc.maxLength} symbols"
                            }
                            else -> {
                                /* NO-OP */
                            }
                        }
                    }
                    return@setStringFromCallback ""
                },
                onValueChange = {
                    viewModel.onEvent(AddNewPlaceEvent.DescriptionChanged(it))
                },
            )
            Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                viewModel.onEvent(AddNewPlaceEvent.AddPlace)
            }) {
                Text(text = "Add")
            }

        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PlaceImageSelector(
    viewModel: AddNewPlaceViewModel,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope
) {
    val context = LocalContext.current
    val storagePermissionState =
        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE)
    val imagePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            try {
                it?.let { uri ->
                    if (SDK_INT < 28) {
                        viewModel.onEvent(
                            AddNewPlaceEvent.ImageChanged(
                                MediaStore.Images
                                    .Media.getBitmap(context.contentResolver, uri)
                            )
                        )

                    } else {
                        val source = ImageDecoder
                            .createSource(context.contentResolver, uri)
                        viewModel.onEvent(
                            AddNewPlaceEvent.ImageChanged(
                                ImageDecoder.decodeBitmap(
                                    source
                                )
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("Unable to choose picture, please try again")
                }
            }
        }
    val storagePermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            imagePickerLauncher.launch(
                "image/*"
            )

        } else {
            /* NO-OP */
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .padding(8.dp)
                .border(
                    1.dp,
                    MaterialTheme.colors.onSurface.copy(
                        alpha = ContentAlpha.disabled
                    ),
                    RoundedCornerShape(5.dp)
                )
                .clickable {
                    if (storagePermissionState.status == PermissionStatus.Granted) {
                        try {
                            imagePickerLauncher.launch(
                                "image/*"
                            )
                        } catch (e: Exception) {
                            Log.e("AddNewPlaceScreen", e.message.toString())
                        }
                    } else {
                        storagePermLauncher.launch(storagePermissionState.permission)
                        if (!storagePermissionState.status.shouldShowRationale) {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "Please enable storage permission from your phone settings "
                                )
                            }
                        }
                    }
                },
        ) {
            val image = viewModel.state.image
            if (image != null) {
                Image(
                    bitmap = image.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier
                            .width(35.dp)
                            .height(35.dp),
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                    Text(
                        text = "Add an image for place (not necessary)",
                        textAlign = TextAlign.Center
                    )
                }
            }

        }
        if (viewModel.state.image != null) {
            TextButton(onClick = {
                viewModel.onEvent(AddNewPlaceEvent.ImageRemoved)
            }) {
                Text(text = "Remove")
            }
        }
    }
}

@Composable
fun LocationSelector(
    viewModel: AddNewPlaceViewModel,
    navigator: DestinationsNavigator,
    locationResultRecipient: ResultRecipient<LocationSelectorMapScreenDestination, LatLng>
) {
    locationResultRecipient.onNavResult {
        val latLng = it.getOr(canceledValue = {
            null
        })
        latLng?.apply {
            viewModel.onEvent(AddNewPlaceEvent.LocationChanged(this))
        }

    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        viewModel.state.location.value.data?.apply {
            Text(
                modifier = Modifier.weight(1f),
                text = String.format(
                    "Selected location:\n" +
                            "lat: %.3f...\n" +
                            "lng: %.3f...\n", this.latitude, this.longitude
                ),
            )
        }
        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                navigator.navigate(LocationSelectorMapScreenDestination(viewModel.state.location.value.data)) {
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = if (viewModel.state.location.value.data == null && viewModel.state.showErrorMessages) {
                ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.error
                )
            } else {
                ButtonDefaults.buttonColors()
            }
        ) {
            Text(text = "Select location")
        }

    }
}

fun setStringFromCallback(callBack: () -> String): String {
    return callBack.invoke()
}