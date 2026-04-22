package com.farmconnect.core.ui.components

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.farmconnect.core.ui.components.textfields.FarmConnectOutlinedTextField
import com.farmconnect.core.ui.theme.FarmConnectTheme
import com.farmconnect.core.ui.theme.LocalPrimaryColor
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.kotlin.awaitFetchPlace
import com.google.android.libraries.places.widget.PlaceAutocomplete
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.launch
import Napie.log.Napie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    modifier: Modifier = Modifier,
    location : List<Double> = emptyList(),
    locationName: String? = null,
    useMyLocation: Boolean = false,
    onLocationSelected: (Double, Double) -> Unit = { _, _ -> },
    onBack: () -> Unit = {},
    onPlaceSelected: (Place) -> Unit = {},
    onUseMyLocationToggled: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val placeAutocompleteActivityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { intent ->
                val prediction = PlaceAutocomplete.getPredictionFromIntent(intent)
                val placesClient: PlacesClient = Places.createClient(context)

                coroutineScope.launch {
                    val response = placesClient.awaitFetchPlace(
                        prediction?.placeId.toString(), listOf(
                            Place.Field.DISPLAY_NAME, Place.Field.LOCATION
                        )
                    )
                    onPlaceSelected(response.place)
                }
            }
        }
    }

    val defaultLocation = if (location.size >= 2) {
        LatLng(location[0], location[1])
    } else {
        LatLng(-0.3031, 36.0800) // Nakuru, Kenya default
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 15f)
    }
    val sessionToken = AutocompleteSessionToken.newInstance()
    val autocompleteIntent = PlaceAutocomplete.createIntent(context) {
        setAutocompleteSessionToken(sessionToken)
    }
    val markerState = rememberUpdatedMarkerState(defaultLocation)

    // Add this state to track if map is loaded
    val isMapLoaded = remember { mutableStateOf(false) }

    // Update camera position only after map is loaded
    LaunchedEffect(location, isMapLoaded.value) {
        if (location.size >= 2 && isMapLoaded.value) {
            try {
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(location[0], location[1]),
                        15f
                    ),
                    durationMs = 1000
                )
            } catch (e: Exception) {
                // Fallback: update position directly if animation fails
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    LatLng(location[0], location[1]),
                    15f
                )
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        // Google Map as background
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = useMyLocation
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = false,
                zoomControlsEnabled = false,
                compassEnabled = false
            ),
            onMapClick = { latLng ->
                markerState.position = latLng
                onLocationSelected(latLng.latitude, latLng.longitude)
                Napie.d("latitude: ${latLng.latitude}, longitude: ${latLng.longitude}")
            },
            onMapLoaded = {
                isMapLoaded.value = true
            }
        ) {
            if (location.size >= 2) {
                Marker(
                    state = markerState,
                    title = locationName ?: "Selected Location"
                )
            }
        }

        // Search and suggestions overlay

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .align(Alignment.TopCenter),
            shadowElevation = 4.dp,
            color = Color.Black.copy(.55f)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp),
                verticalAlignment = Alignment.Top,
            ) {

                IconButton(
                    onClick = onBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = LocalPrimaryColor.current,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = null
                    )
                }


                Column(
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    FarmConnectOutlinedTextField(
                        shape = RoundedCornerShape(50),
                        enabled = false,
                        modifier = Modifier
                            .height(40.dp)
                            .clickable {
                                placeAutocompleteActivityResultLauncher.launch(
                                    autocompleteIntent
                                )
                            },
                        value = locationName ?: "",
                        onValueChange = {},
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = null,
                                modifier = Modifier.size(35.dp)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                    4
                }
            }
        }

        // Map controls at bottom right
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                onClick = { onUseMyLocationToggled(!useMyLocation) },
                shape = CircleShape,
                shadowElevation = 4.dp,
                color = if (useMyLocation)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surface
            ) {
                Icon(
                    imageVector = Icons.Rounded.MyLocation,
                    contentDescription = "Use my location",
                    modifier = Modifier
                        .padding(12.dp)
                        .size(24.dp),
                    tint = if (useMyLocation)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }

            if (locationName != null) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 4.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = locationName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (location.size >= 2) {
                            Text(
                                text = "${
                                    String.format(
                                        "%.4f",
                                        location[0]
                                    )
                                }, ${String.format("%.4f", location[1])}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LocationScreenPrev() {
    FarmConnectTheme {
        LocationScreen()
    }
}